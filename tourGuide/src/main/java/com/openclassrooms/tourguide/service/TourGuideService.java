package com.openclassrooms.tourguide.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.openclassrooms.tourguide.dto.AttractionNearUserDto;
import com.openclassrooms.tourguide.dto.NearbyAttractionDto;
import com.openclassrooms.tourguide.dto.UserLocationDto;
import com.openclassrooms.tourguide.model.Reward;
import com.openclassrooms.tourguide.model.User;
import com.openclassrooms.tourguide.util.InternalUsersInitializer;
import com.openclassrooms.tourguide.util.UsersTracker;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
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
public class TourGuideService implements ITourGuideService {
	private static final Logger logger = LoggerFactory.getLogger(TourGuideService.class);
	private static final String tripPricerApiKey = "test-server-api-key";
	
	private final GpsUtil gpsUtil;
	private final RewardsService rewardsService;
	private final TripPricer tripPricer = new TripPricer();
	private final InternalUsersInitializer internalUsers = new InternalUsersInitializer();
	public final UsersTracker tracker;

	private boolean testMode = true;
	private Map<String, User> internalUserMap;

	public TourGuideService(GpsUtil gpsUtil, RewardsService rewardsService) {
		this.gpsUtil = gpsUtil;
		this.rewardsService = rewardsService;

		Locale.setDefault(Locale.US);

		if (testMode) {
			logger.info("TestMode enabled.");
			logger.debug("Initializing users.");
			internalUserMap = internalUsers.initializeInternalUsers();
			logger.debug("Finished initializing users.");
		}
		tracker = new UsersTracker(this);
		addShutDownHook();
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
	@Override
	public List<User> getAllUsers() {
		return internalUserMap.values().stream().collect(Collectors.toList());
	}

	/**
	 * A <code>GET</code> method that returns a <code>User</code> whose username is
	 * passed as parameter after calling the getter of the <code>Map<String, User></code>
	 * 
	 * @warning <code>internalUserMap</code> is initialized below.
	 * 
	 * @return A <code>User</code>.
	 */
	@Override
	public User getUser(String userName) {
		return internalUserMap.get(userName);
	}

	/**
	 * A <code>GET</code> method that returns the <code>VisitedLocation</code> for
	 * the <code>User</code> passed as a parameter after, if he has already visited
	 * some locations. Otherwise, he returns the actual location. calling the
	 * <code>VisitedLocations</code> list getter of <code>User</code> class.
	 * 
	 * @return A <code>UserReward</code> list.
	 */
	@Override
	public VisitedLocation getUserLocation(User user) {
		VisitedLocation visitedLocation = (user.getVisitedLocations().size() > 0) ? user.getLastVisitedLocation() : trackUserLocation(user);
		return visitedLocation;
	}

	/**
	 * A <code>GET</code> method that returns an <code>Attraction</code> list of all
	 * attractions near the <code>VisitedLocation</code> passed as parameter
	 * 
	 * @return A <code>Attraction</code> list.
	 */
	@Override
	public List<Attraction> getNearByAttractions(VisitedLocation visitedLocation) {
		List<Attraction> nearbyAttractions = new ArrayList<>();
		TreeMap<Double, Attraction> attractionsOrderByDistance = new TreeMap<>(); 
		
		for(Attraction attraction : gpsUtil.getAttractions()) {
			double distanceBetween = rewardsService.getDistance(visitedLocation.location, attraction);
			attractionsOrderByDistance.put(distanceBetween, attraction);	
		}
		
		for(Map.Entry<Double, Attraction> entry : attractionsOrderByDistance.entrySet()) {
			nearbyAttractions.add(entry.getValue());
		}
		
		return nearbyAttractions.stream().limit(5).collect(Collectors.toList());
	}
	
	@Override
	public List<AttractionNearUserDto> formatNearbyAttractions(User user, VisitedLocation visitedLocation, List<Attraction> nearbyAttractions) {
		UserLocationDto userLocationDto = new UserLocationDto(visitedLocation.location.latitude, visitedLocation.location.longitude);
		List<AttractionNearUserDto> formatedNearbyAttractions = new ArrayList<>();
		
		for (Attraction attraction : nearbyAttractions) {
			NearbyAttractionDto nearbyAttractionDto = new NearbyAttractionDto(attraction.attractionName, attraction.latitude, attraction.longitude);
			
			AttractionNearUserDto userRelatedAttraction = new AttractionNearUserDto();
			userRelatedAttraction.setNearbyAttraction(nearbyAttractionDto);
			userRelatedAttraction.setUserLocation(userLocationDto);
			userRelatedAttraction.setDistanceBetween(rewardsService.getDistance(visitedLocation.location, attraction));
			userRelatedAttraction.setRewardPoints(rewardsService.getRewardPoints(attraction, user));
			
			formatedNearbyAttractions.add(userRelatedAttraction);
		}

		return formatedNearbyAttractions;
	}

	/**
	 * A <code>GET</code> method that returns a <code>UserReward</code> list for the
	 * <code>User</code> passed as a parameter after calling the
	 * <code>UserReward</code> getter of <code>User</code> class.
	 * 
	 * @return A <code>UserReward</code> list.
	 */
	@Override
	public List<Reward> getUserRewards(User user) {
		return user.getUserRewards();
	}

	/**
	 * A <code>GET</code> method that returns a <code>Provider</code> list of
	 * provides offering trip deals based on the number of reward points the
	 * <code>User</code> passed in parameter has accumulated.
	 * 
	 * @return A <code>Provider</code> list.
	 */
	@Override
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
	 * A <code>POST</code> method that returns a <code>User</code> passed in
	 * parameter and put into the <code>internalUserMap</code> declared below, if
	 * and only if the <code>internalUserMap</code> doesn't contain it.
	 * 
	 * @warning <code>internalUserMap</code> is initialized below.
	 * 
	 * @return A <code>User</code>.
	 */
	@Override
	public void addUser(User user) {
		if (!internalUserMap.containsKey(user.getUserName())) {
			internalUserMap.put(user.getUserName(), user);
		}
	}

	/**
	 * A method that gets the current location of an <code>User</code> passed as
	 * parameter, then add it to a list of visited locations and update the reward
	 * score.
	 * 
	 * @return The actual <code>VisitedLocation</code>.
	 */
	@Override
	public VisitedLocation trackUserLocation(User user) {
		VisitedLocation visitedLocation = gpsUtil.getUserLocation(user.getUserId());
		user.addToVisitedLocations(visitedLocation);
		rewardsService.calculateRewards(user);
		
		return visitedLocation;
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
}