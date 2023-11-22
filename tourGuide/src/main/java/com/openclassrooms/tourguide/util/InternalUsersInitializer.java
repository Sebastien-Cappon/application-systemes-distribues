package com.openclassrooms.tourguide.util;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.openclassrooms.tourguide.constant.InternalUsersQuantity;
import com.openclassrooms.tourguide.model.User;

import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;

public class InternalUsersInitializer {
	private static final Logger logger = LoggerFactory.getLogger(InternalUsersInitializer.class);
	
	/**
	 * A method that creates users in memory for internal testing.
	 * 
	 * @return <code>void</code>.
	 */
	public Map<String, User> initializeInternalUsers() {
		Map<String, User> internalUserMap = new HashMap<>();
		
		IntStream.range(0, InternalUsersQuantity.getInternalUserNumber()).forEach(i -> {
			String userName = "internalUser" + i;
			String phone = "000";
			String email = userName + "@tourGuide.com";
			User user = new User(UUID.randomUUID(), userName, phone, email);
			generateUserLocationHistory(user);

			internalUserMap.put(userName, user);
		});
		logger.debug("Created " + InternalUsersQuantity.getInternalUserNumber() + " internal test users.");
		
		return internalUserMap;
	}

	/**
	 * A method that creates users location history, from a set of random longitudes
	 * and latitudes, in memory for internal testing.
	 * 
	 * @return <code>void</code>.
	 */
	public void generateUserLocationHistory(User user) {
		IntStream.range(0, 3).forEach(i -> {
			user.addToVisitedLocations(new VisitedLocation(user.getUserId(), new Location(generateRandomLatitude(), generateRandomLongitude()), getRandomTime()));
		});
	}

	/**
	 * A method that generate random longitude between -180 and 180.
	 * 
	 * @return A <code>double</code> which is longitude.
	 */
	public double generateRandomLongitude() {
		double leftLimit = -180;
		double rightLimit = 180;
		return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
	}

	/**
	 * A method that generate random latitude between -85.05112878 and 85.05112878.
	 * 
	 * @return A <code>double</code> which is latitude.
	 */
	public double generateRandomLatitude() {
		double leftLimit = -85.05112878;
		double rightLimit = 85.05112878;
		return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
	}

	/**
	 * A method that generate random datetime.
	 * 
	 * @return A <code>Date</code>.
	 */
	public Date getRandomTime() {
		LocalDateTime localDateTime = LocalDateTime.now().minusDays(new Random().nextInt(30));
		return Date.from(localDateTime.toInstant(ZoneOffset.UTC));
	}
}