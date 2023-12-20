package com.openclassrooms.tourguide.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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
 * @author [NPC]TourGuide BackEnd Team, Sébastien Cappon
 * @version 1.1
 */
@Service
public class RewardService implements IRewardService {
	private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;

	private final GpsUtil gpsUtil;
	private final RewardCentral rewardCentral;

	private int defaultProximityBuffer = 10;
	private int proximityBuffer = defaultProximityBuffer;
	private int attractionProximityRange = 200;
	private ExecutorService executorService = Executors.newFixedThreadPool(50);

	public RewardService(GpsUtil gpsUtil, RewardCentral rewardCentral) {
		this.gpsUtil = gpsUtil;
		this.rewardCentral = rewardCentral;
	}

	@Override
	public void setDefaultProximityBuffer() {
		proximityBuffer = defaultProximityBuffer;
	}

	@Override
	public void setProximityBuffer(int proximityBuffer) {
		this.proximityBuffer = proximityBuffer;
	}

	/**
	 * A method that calculates the distance, in nautical miles, between to
	 * <code>Location</code>s passed as parameters
	 * 
	 * @return A <code>Double</code> that represents the distance.
	 */
	@Override
	public double getDistance(Location firstLocation, Location secondLocation) {
		double firstLocationLatitude = Math.toRadians(firstLocation.latitude);
		double firstLocationLongitude = Math.toRadians(firstLocation.longitude);
		double secondLocationLatitude = Math.toRadians(secondLocation.latitude);
		double secondLocationLongitude = Math.toRadians(secondLocation.longitude);

		double angle = Math.acos(
				Math.sin(firstLocationLatitude)* Math.sin(secondLocationLatitude) + Math.cos(firstLocationLatitude) * Math.cos(secondLocationLatitude) * Math.cos(firstLocationLongitude - secondLocationLongitude));

		double nauticalMiles = 60 * Math.toDegrees(angle);
		double statuteMiles = STATUTE_MILES_PER_NAUTICAL_MILE * nauticalMiles;

		return statuteMiles;
	}

	/**
	 * A method that assigns reward points to a <code>User</code> for a given
	 * <code>Attraction</code>
	 * 
	 * @return The number of points earned, as <code>integer/<code>.
	 */
	public int getRewardPoints(Attraction attraction, User user) {
		return rewardCentral.getAttractionRewardPoints(attraction.attractionId, user.getUserId());
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
	 * attraction he has visited around his current location and return the modified
	 * <code>User</code> for further performance enhancement.
	 * 
	 * @return A <code>User</code>.
	 */
	@Override
	public User manageUserRewardList(User user) {
		List<Attraction> attractions = gpsUtil.getAttractions();
		CopyOnWriteArrayList<VisitedLocation> userVisitedLocations = new CopyOnWriteArrayList<>(user.getVisitedLocationList());

		for (VisitedLocation visitedLocation : userVisitedLocations) {
			for (Attraction attraction : attractions) {
				if (user.getUserRewardList().stream().filter(reward -> reward.attraction.attractionName.equals(attraction.attractionName)).count() == 0) {
					if (nearAttraction(visitedLocation, attraction)) {
						user.addReward(new Reward(visitedLocation, attraction, getRewardPoints(attraction, user)));
					}
				}
			}
		}

		return user;
	}

	/**
	 * A method that allows the <code>manageUserRewardList()</code> method to be
	 * executed asynchronously on a list of users in order to improve the
	 * application's performance.
	 * 
	 * @throws InterruptedException
	 * @return A <code>User</code> list.
	 */
	public List<User> manageEachUserRewardList(List<User> users) throws InterruptedException {
		List<User> usersList = new ArrayList<>();

		for (User user : users) {
			executorService.execute(() -> {
				usersList.add(manageUserRewardList(user));
			});
		}
		executorService.shutdown();
		executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);

		return usersList;
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