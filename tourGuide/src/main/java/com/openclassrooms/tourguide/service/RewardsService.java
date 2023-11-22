package com.openclassrooms.tourguide.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.openclassrooms.tourguide.model.Reward;
import com.openclassrooms.tourguide.model.User;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import rewardCentral.RewardCentral;

/**
 * A service class which performs the business processes relating to the user
 * rewards system. This service used by TourGuideService only.
 * 
 * @author [NPC]TourGuide BackEnd Team
 * @version 1.0
 */
@Service
public class RewardsService implements IRewardService {
	private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;

	private final GpsUtil gpsUtil;
	private final RewardCentral rewardsCentral;
	
	private int defaultProximityBuffer = 10;
	private int proximityBuffer = defaultProximityBuffer;
	private int attractionProximityRange = 200;
	
	public RewardsService(GpsUtil gpsUtil, RewardCentral rewardCentral) {
		this.gpsUtil = gpsUtil;
		this.rewardsCentral = rewardCentral;
	}

	public void setProximityBuffer(int proximityBuffer) {
		this.proximityBuffer = proximityBuffer;
	}

	public void setDefaultProximityBuffer() {
		proximityBuffer = defaultProximityBuffer;
	}

	/**
	 * A method that calculates the distance, in nautical mile, between to
	 * <code>Location</code>s passed as parameters
	 * 
	 * @return A <code>Double</code> that represents the distance.
	 */
	@Override
	public double getDistance(Location loc1, Location loc2) {
		double lat1 = Math.toRadians(loc1.latitude);
		double lon1 = Math.toRadians(loc1.longitude);
		double lat2 = Math.toRadians(loc2.latitude);
		double lon2 = Math.toRadians(loc2.longitude);

		double angle = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));

		double nauticalMiles = 60 * Math.toDegrees(angle);
		double statuteMiles = STATUTE_MILES_PER_NAUTICAL_MILE * nauticalMiles;
		return statuteMiles;
	}
	
	/**
	 * A boolean method that determines whether an <code>Attraction</code> is close
	 * to a <code>Location</code>. It takes the <code>Attraction</code> and the
	 * <code>Location</code> as parameters.
	 * 
	 * @return A <code>boolean</code>.
	 */
	@Override
	public boolean isWithinAttractionProximity(Attraction attraction, Location location) {
		return getDistance(attraction, location) > attractionProximityRange ? false : true;
	}
	
	/**
	 * A method that adds reward to a <code>User</code> passed in parameter for each
	 * attraction he has done in locations he has visited around his current
	 * location.
	 * 
	 * @return <code>void</code>.
	 */
	@Override
	public void calculateRewards(User user) {
		List<Attraction> attractions = gpsUtil.getAttractions();
		List<VisitedLocation> userVisitedLocations = user.getVisitedLocations();
		List<VisitedLocation> userVisitedLocationList = new ArrayList<>(userVisitedLocations);
		List<Reward> userRewardList = new ArrayList<>();
		
		for (VisitedLocation visitedLocation : userVisitedLocationList) {
			for (Attraction attraction : attractions) {
				if (userRewardList.stream().filter(r -> r.attraction.attractionName.equals(attraction.attractionName)).count() == 0) {
					if (nearAttraction(visitedLocation, attraction)) {
						userRewardList.add(new Reward(visitedLocation, attraction, getRewardPoints(attraction, user)));
					}
				}
			}
		}
		
		// BUG WORKAROUND ?
		if(user.getUserRewards().isEmpty()) {
			user.getUserRewards().addAll(userRewardList);
		}
	}

	/**
	 * A method that assigns reward points to a <code>User</code> for a given
	 * <code>Attraction</code>
	 * 
	 * @return The number of points earned, as <code>integer/<code>.
	 */
	public int getRewardPoints(Attraction attraction, User user) {
		return rewardsCentral.getAttractionRewardPoints(attraction.attractionId, user.getUserId());
	}
	
	/**
	 * A boolean method that determines whether an <code>Attraction</code> is near
	 * an <code>Attraction</code> the user has visited. It takes the
	 * <code>Attraction</code> and the <code>VisitedLocation</code> as parameters.
	 * 
	 * @return A <code>boolean</code>.
	 */
	private boolean nearAttraction(VisitedLocation visitedLocation, Attraction attraction) {
		return getDistance(attraction, visitedLocation.location) > proximityBuffer ? false : true;
	}
}