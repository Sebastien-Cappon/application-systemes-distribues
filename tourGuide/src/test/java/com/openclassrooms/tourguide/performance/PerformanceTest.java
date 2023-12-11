package com.openclassrooms.tourguide.performance;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.openclassrooms.tourguide.constant.InternalUsersQuantity;
import com.openclassrooms.tourguide.model.User;
import com.openclassrooms.tourguide.service.RewardService;
import com.openclassrooms.tourguide.service.TourGuideService;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import rewardCentral.RewardCentral;

public class PerformanceTest {
	
	private GpsUtil gpsUtil = new GpsUtil();
	private RewardService rewardsService = new RewardService(gpsUtil, new RewardCentral());
	private StopWatch stopWatch = new StopWatch();
	
	@BeforeEach
	public void setupTests() {
		InternalUsersQuantity.setInternalUserQuantity(100);
	}
	
	@Test
	public void highVolumeTrackLocation() throws InterruptedException {
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService);
		
		List<User> allUsers = new ArrayList<>();
		allUsers = tourGuideService.getUserList();

		stopWatch.start();
			tourGuideService.trackEachUserLocation(allUsers);
		stopWatch.stop();
		
		tourGuideService.tracker.stopTracking();
		assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
		System.out.println("highVolumeTrackLocation: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
	}

	
	@Test
	public void highVolumeGetRewards() throws InterruptedException {
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService);
		
		stopWatch.start();
			Attraction attraction = gpsUtil.getAttractions().get(0);
			List<User> userList = new ArrayList<>();
			
			userList = tourGuideService.getUserList();
			userList.forEach(user -> user.addVisitedLocation(new VisitedLocation(user.getUserId(), attraction, new Date())));
			userList = rewardsService.manageEachUserRewardList(userList);
			
			for (User user : userList) {
				assertTrue(user.getUserRewardList().size() > 0);
			}
		stopWatch.stop();

		tourGuideService.tracker.stopTracking();
		assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
		System.out.println("highVolumeGetRewards: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
	}

}
