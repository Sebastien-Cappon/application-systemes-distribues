package com.openclassrooms.tourguide.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.openclassrooms.tourguide.constant.InternalUsersQuantity;
import com.openclassrooms.tourguide.model.User;
import com.openclassrooms.tourguide.model.Reward;
import com.openclassrooms.tourguide.service.RewardsService;
import com.openclassrooms.tourguide.service.TourGuideService;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import rewardCentral.RewardCentral;

public class TestRewardsService {

	@Test
	public void userGetRewards() {
		GpsUtil gpsUtil = new GpsUtil();
		RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());

		InternalUsersQuantity.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService);

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		Attraction attraction = gpsUtil.getAttractions().get(0);
		user.addToVisitedLocations(new VisitedLocation(user.getUserId(), attraction, new Date()));
		tourGuideService.trackUserLocation(user);
		List<Reward> userRewards = user.getUserRewards();
		tourGuideService.tracker.stopTracking();
		assertTrue(userRewards.size() == 1);
	}

	@Test
	public void isWithinAttractionProximity() {
		GpsUtil gpsUtil = new GpsUtil();
		RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());
		Attraction attraction = gpsUtil.getAttractions().get(0);
		assertTrue(rewardsService.isWithinAttractionProximity(attraction, attraction));
	}

	@Test
	public void nearAllAttractions() {
		GpsUtil gpsUtil = new GpsUtil();																			System.out.println("J1");
		RewardsService rewardsService = new RewardsService(gpsUtil, new RewardCentral());							System.out.println("J2");
		rewardsService.setProximityBuffer(Integer.MAX_VALUE);														System.out.println("J3");

		InternalUsersQuantity.setInternalUserNumber(1);																System.out.println("J4");
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService);							System.out.println("J5");

		rewardsService.calculateRewards(tourGuideService.getAllUsers().get(0));										System.out.println("J6");
		List<Reward> userRewards = tourGuideService.getUserRewards(tourGuideService.getAllUsers().get(0));			System.out.println("J7");
		tourGuideService.tracker.stopTracking();																	System.out.println("J8");

		assertEquals(gpsUtil.getAttractions().size(), userRewards.size());											System.out.println("J9");
	}

}
