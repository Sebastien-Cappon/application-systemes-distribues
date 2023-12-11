package com.openclassrooms.tourguide.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.openclassrooms.tourguide.constant.InternalUsersQuantity;
import com.openclassrooms.tourguide.model.Reward;
import com.openclassrooms.tourguide.model.User;
import com.openclassrooms.tourguide.service.IRewardService;
import com.openclassrooms.tourguide.service.ITourGuideService;
import com.openclassrooms.tourguide.service.RewardService;
import com.openclassrooms.tourguide.service.TourGuideService;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import rewardCentral.RewardCentral;

public class RewardServiceIT {

	private GpsUtil gpsUtil = new GpsUtil();
	private IRewardService iRewardService = new RewardService(gpsUtil, new RewardCentral());
	private ITourGuideService iTourGuideService = new TourGuideService(gpsUtil, iRewardService);

	@Test
	public void userGetRewards() {
		InternalUsersQuantity.setInternalUserQuantity(0);

		Attraction attraction = gpsUtil.getAttractions().get(0);
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");

		user.addVisitedLocation(new VisitedLocation(user.getUserId(), attraction, new Date()));
		iTourGuideService.trackUserLocation(user);
		List<Reward> userRewards = user.getUserRewardList();

		iTourGuideService.getTracker().stopTracking();
		assertTrue(userRewards.size() == 1);
	}

	@Test
	public void isWithinAttractionProximity() {
		Attraction attraction = gpsUtil.getAttractions().get(0);
		assertTrue(iRewardService.isWithinAttractionProximity(attraction, attraction));
	}

	@Test
	public void nearAllAttractions() {
		iRewardService.setProximityBuffer(Integer.MAX_VALUE);
		InternalUsersQuantity.setInternalUserQuantity(1);
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, iRewardService);

		iRewardService.manageUserRewardList(tourGuideService.getUserList().get(0));
		List<Reward> userRewards = tourGuideService.getUserRewardList(tourGuideService.getUserList().get(0));

		tourGuideService.tracker.stopTracking();
		assertEquals(gpsUtil.getAttractions().size(), userRewards.size());
	}
}