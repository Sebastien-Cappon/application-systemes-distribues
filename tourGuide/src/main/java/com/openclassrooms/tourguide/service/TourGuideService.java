package com.openclassrooms.tourguide.service;

import com.openclassrooms.tourguide.helper.InternalTestHelper;
import com.openclassrooms.tourguide.tracker.Tracker;
import com.openclassrooms.tourguide.user.User;
import com.openclassrooms.tourguide.user.UserReward;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;

import tripPricer.Provider;
import tripPricer.TripPricer;

/**
 * A service class which performs the business processes ??? This service used
 * by ??? only.
 * 
 * @author [NPC]TourGuide BackEnd Team
 * @version 1.0
 */
@Service
public class TourGuideService {
	private Logger logger = LoggerFactory.getLogger(TourGuideService.class);
	private final GpsUtil gpsUtil;
	private final RewardsService rewardsService;
	private final TripPricer tripPricer = new TripPricer();
	public final Tracker tracker;
	boolean testMode = true;

	public TourGuideService(GpsUtil gpsUtil, RewardsService rewardsService) {
		this.gpsUtil = gpsUtil;
		this.rewardsService = rewardsService;

		Locale.setDefault(Locale.US);

		if (testMode) {
			logger.info("TestMode enabled.");
			logger.debug("Initializing users.");
			initializeInternalUsers();
			logger.debug("Finished initializing users.");
		}
		tracker = new Tracker(this);
		addShutDownHook();
	}

	/**
	 * A <code>GET</code> method that returns a <code>UserReward</code> list for the
	 * <code>User</code> passed as a parameter after calling the
	 * <code>UserReward</code> getter of <code>User</code> class.
	 * 
	 * @return A <code>UserReward</code> list.
	 */
	public List<UserReward> getUserRewards(User user) {
		return user.getUserRewards();
	}

	/**
	 * A <code>GET</code> method that returns the <code>VisitedLocation</code> for
	 * the <code>User</code> passed as a parameter after, if he has already visited
	 * some locations. Otherwise, he returns the actual location. calling the
	 * <code>VisitedLocations</code> list getter of <code>User</code> class.
	 * 
	 * @return A <code>UserReward</code> list.
	 */
	public VisitedLocation getUserLocation(User user) {
		VisitedLocation visitedLocation = (user.getVisitedLocations().size() > 0) ? user.getLastVisitedLocation() : trackUserLocation(user);
		return visitedLocation;
	}

	/**
	 * A <code>GET</code> method that returns a <code>User</code> whose username is
	 * passed as parameter after calling the getter of the <code>Map<String, User></code>
	 * 
	 * @warning <code>internalUserMap</code> is initialized below.
	 * 
	 * @return A <code>User</code>.
	 */
	public User getUser(String userName) {
		return internalUserMap.get(userName);
	}

	/**
	 * A <code>GET</code> method that returns a list of <code>User</code>. It puts
	 * the <code>internalUserMap</code> declared below in a stream and get the
	 * response in a List Collection.
	 * 
	 * @warning <code>internalUserMap</code> is initialized below.
	 * 
	 * @return A <code>User</code>.
	 */
	public List<User> getAllUsers() {
		return internalUserMap.values().stream().collect(Collectors.toList());
	}

	/**
	 * A <code>POST</code> method that returns a <code>User</code> passed in
	 * parameter and put into the <code>internalUserMap</code> declared below, if
	 * and only if the <code>internalUserMap</code> doesn't contain it.
	 * 
	 * @warning <code>internalUserMap</code> is initialized below.
	 * 
	 * @return A <code>User</code>.
	 */
	public void addUser(User user) {
		if (!internalUserMap.containsKey(user.getUserName())) {
			internalUserMap.put(user.getUserName(), user);
		}
	}

	/**
	 * A <code>GET</code> method that returns a <code>Provider</code> list of
	 * provides offering trip deals based on the number of reward points the
	 * <code>User</code> passed in parameter has accumulated.
	 * 
	 * @return A <code>Provider</code> list.
	 */
	public List<Provider> getTripDeals(User user) {
		int cumulatativeRewardPoints = user.getUserRewards().stream().mapToInt(i -> i.getRewardPoints()).sum();
		List<Provider> providers = tripPricer.getPrice(
				tripPricerApiKey,
				user.getUserId(),
				user.getUserPreferences().getNumberOfAdults(),
				user.getUserPreferences().getNumberOfChildren(),
				user.getUserPreferences().getTripDuration(),
				cumulatativeRewardPoints
			);
		user.setTripDeals(providers);
		return providers;
	}

	
	/**
	 * A method that gets the current location of an <code>User</code> passed as
	 * parameter, then add it to a list of visited locations and update the reward
	 * score.
	 * 
	 * @return The actual <code>VisitedLocation</code>.
	 */
	public VisitedLocation trackUserLocation(User user) {
		VisitedLocation visitedLocation = gpsUtil.getUserLocation(user.getUserId());
		user.addToVisitedLocations(visitedLocation);
		rewardsService.calculateRewards(user);
		return visitedLocation;
	}

	/**
	 * A <code>GET</code> method that returns an <code>Attraction</code> list of all
	 * attractions near the <code>VisitedLocation</code> passed as parameter
	 * 
	 * @return A <code>Attraction</code> list.
	 */
	public List<Attraction> getNearByAttractions(VisitedLocation visitedLocation) {
		List<Attraction> nearbyAttractions = new ArrayList<>();
		for (Attraction attraction : gpsUtil.getAttractions()) {
			if (rewardsService.isWithinAttractionProximity(attraction, visitedLocation.location)) {
				nearbyAttractions.add(attraction);
			}
		}

		return nearbyAttractions;
	}

	/**
	 * A method that closes the tracker task.
	 * 
	 * @return <code>void</code>.
	 */
	private void addShutDownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				tracker.stopTracking();
			}
		});
	}

	/**********************************************************************************
	 * 
	 * Methods Below: For Internal Testing
	 * 
	 **********************************************************************************/
	private static final String tripPricerApiKey = "test-server-api-key";
	// Database connection will be used for external users, but for testing purposes
	// internal users are provided and stored in memory
	private final Map<String, User> internalUserMap = new HashMap<>();

	/**
	 * A method that creates users in memory for internal testing.
	 * 
	 * @return <code>void</code>.
	 */
	private void initializeInternalUsers() {
		IntStream.range(0, InternalTestHelper.getInternalUserNumber()).forEach(i -> {
			String userName = "internalUser" + i;
			String phone = "000";
			String email = userName + "@tourGuide.com";
			User user = new User(UUID.randomUUID(), userName, phone, email);
			generateUserLocationHistory(user);

			internalUserMap.put(userName, user);
		});
		logger.debug("Created " + InternalTestHelper.getInternalUserNumber() + " internal test users.");
	}

	/**
	 * A method that creates users location history, from a set of random longitudes
	 * and latitudes, in memory for internal testing.
	 * 
	 * @return <code>void</code>.
	 */
	private void generateUserLocationHistory(User user) {
		IntStream.range(0, 3).forEach(i -> {
			user.addToVisitedLocations(new VisitedLocation(user.getUserId(),
					new Location(generateRandomLatitude(), generateRandomLongitude()), getRandomTime()));
		});
	}

	/**
	 * A method that generate random longitude between -180 and 180.
	 * 
	 * @return A <code>double</code> which is longitude.
	 */
	private double generateRandomLongitude() {
		double leftLimit = -180;
		double rightLimit = 180;
		return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
	}

	/**
	 * A method that generate random latitude between -85.05112878 and 85.05112878.
	 * 
	 * @return A <code>double</code> which is latitude.
	 */
	private double generateRandomLatitude() {
		double leftLimit = -85.05112878;
		double rightLimit = 85.05112878;
		return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
	}

	/**
	 * A method that generate random datetime.
	 * 
	 * @return A <code>Date</code>.
	 */
	private Date getRandomTime() {
		LocalDateTime localDateTime = LocalDateTime.now().minusDays(new Random().nextInt(30));
		return Date.from(localDateTime.toInstant(ZoneOffset.UTC));
	}
}