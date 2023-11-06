package com.openclassrooms.tourguide.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.openclassrooms.tourguide.user.User;
import com.openclassrooms.tourguide.user.UserReward;

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
public class RewardsService {
	private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;

	// proximity in miles
	private int defaultProximityBuffer = 10;
	private int proximityBuffer = defaultProximityBuffer;
	private int attractionProximityRange = 200;
	private final GpsUtil gpsUtil;
	private final RewardCentral rewardsCentral;

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
	 * A method that adds reward to a <code>User</code> passed in parameter for each
	 * attraction he has done in locations he has visited around his current
	 * location.
	 * 
	 * @warning Refactor it in order to resolve the <code>ConcurrentModificationException</code> on
	 *          <code>nearAllAttractions()</code> test method from <code>TestRewardService</code> class.
	 *          A method mustn't do several things at once. Split it in 2 methods (filter, then reward).
	 *
	 * @return <code>void</code>.
	 */
	public void calculateRewards(User user) {
		List<VisitedLocation> userLocations = user.getVisitedLocations();
		List<Attraction> attractions = gpsUtil.getAttractions();

		for (VisitedLocation visitedLocation : userLocations) {
			for (Attraction attraction : attractions) {
				if (user.getUserRewards().stream().filter(r -> r.attraction.attractionName.equals(attraction.attractionName)).count() == 0) {
					if (nearAttraction(visitedLocation, attraction)) {
						user.addUserReward(new UserReward(visitedLocation, attraction, getRewardPoints(attraction, user)));
					}
				}
			}
		}
	}

	/**
	 * A boolean method that determines whether an <code>Attraction</code> is close
	 * to a <code>Location</code>. It takes the <code>Attraction</code> and the
	 * <code>Location</code> as parameters.
	 * 
	 * @return A <code>boolean</code>.
	 */
	public boolean isWithinAttractionProximity(Attraction attraction, Location location) {
		return getDistance(attraction, location) > attractionProximityRange ? false : true;
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

	/**
	 * A method that assigns reward points to a <code>User</code> for a given
	 * <code>Attraction</code>
	 * 
	 * @return The number of points earned, as <code>integer/<code>.
	 */
	private int getRewardPoints(Attraction attraction, User user) {
		return rewardsCentral.getAttractionRewardPoints(attraction.attractionId, user.getUserId());
	}

	/**
	 * A method that calculates the distance, in nautical mile, between to
	 * <code>Location</code>s passed as parameters
	 * 
	 * @return A <code>Double</code> that represents the distance.
	 */
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
}