package com.openclassrooms.tourguide.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.openclassrooms.tourguide.constant.InternalUsersQuantity;
import com.openclassrooms.tourguide.dto.AttractionNearUserDto;
import com.openclassrooms.tourguide.model.User;
import com.openclassrooms.tourguide.service.IRewardService;
import com.openclassrooms.tourguide.service.ITourGuideService;
import com.openclassrooms.tourguide.service.RewardService;
import com.openclassrooms.tourguide.service.TourGuideService;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import rewardCentral.RewardCentral;
import tripPricer.Provider;

public class TourGuideServiceIT {

	private GpsUtil gpsUtil = new GpsUtil();
	private IRewardService iRewardService = new RewardService(gpsUtil, new RewardCentral());

	@BeforeEach
	public void setupTests() {
		InternalUsersQuantity.setInternalUserQuantity(100);
	}

	@Test
	public void getUserLocation() {
		ITourGuideService iTourGuideService = new TourGuideService(gpsUtil, iRewardService);

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		VisitedLocation visitedLocation = iTourGuideService.trackUserLocation(user);

		iTourGuideService.getTracker().stopTracking();
		assertTrue(visitedLocation.userId.equals(user.getUserId()));
	}

	@Test
	public void addUser() {
		ITourGuideService iTourGuideService = new TourGuideService(gpsUtil, iRewardService);

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");

		iTourGuideService.addUser(user);
		iTourGuideService.addUser(user2);

		User retrievedUser = iTourGuideService.getUserByName(user.getName());
		User retrievedUser2 = iTourGuideService.getUserByName(user2.getName());

		iTourGuideService.getTracker().stopTracking();
		assertEquals(user, retrievedUser);
		assertEquals(user2, retrievedUser2);
	}

	@Test
	public void getAllUsers() {
		ITourGuideService iTourGuideService = new TourGuideService(gpsUtil, iRewardService);

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");

		iTourGuideService.addUser(user);
		iTourGuideService.addUser(user2);

		List<User> allUsers = iTourGuideService.getUserList();

		iTourGuideService.getTracker().stopTracking();
		assertTrue(allUsers.contains(user));
		assertTrue(allUsers.contains(user2));
	}

	@Test
	public void trackUser() {
		ITourGuideService iTourGuideService = new TourGuideService(gpsUtil, iRewardService);

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		VisitedLocation visitedLocation = iTourGuideService.trackUserLocation(user);

		iTourGuideService.getTracker().stopTracking();

		assertEquals(user.getUserId(), visitedLocation.userId);
	}

	@Test
	public void getNearbyAttractions() {
		ITourGuideService iTourGuideService = new TourGuideService(gpsUtil, iRewardService);

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		VisitedLocation visitedLocation = iTourGuideService.trackUserLocation(user);
		List<Attraction> nearbyAttractions = iTourGuideService.getNearbyAttractionList(visitedLocation);
		List<AttractionNearUserDto> formatedNearbyAttractions = iTourGuideService.formatNearbyAttractionList(user, visitedLocation, nearbyAttractions);

		iTourGuideService.getTracker().stopTracking();
		assertEquals(5, formatedNearbyAttractions.size());
	}

	@Test
	public void getTripDeals() {
		ITourGuideService iTourGuideService = new TourGuideService(gpsUtil, iRewardService);

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		List<Provider> providers = iTourGuideService.getTripDealList(user);

		iTourGuideService.getTracker().stopTracking();
		assertEquals(5, providers.size());
	}
}