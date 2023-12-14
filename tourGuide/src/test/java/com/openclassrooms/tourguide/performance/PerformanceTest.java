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
import com.openclassrooms.tourguide.service.IRewardService;
import com.openclassrooms.tourguide.service.ITourGuideService;
import com.openclassrooms.tourguide.service.RewardService;
import com.openclassrooms.tourguide.service.TourGuideService;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import rewardCentral.RewardCentral;

public class PerformanceTest {

	private GpsUtil gpsUtil = new GpsUtil();
	private IRewardService iRewardsService = new RewardService(gpsUtil, new RewardCentral());
	private StopWatch stopWatch = new StopWatch();

	@BeforeEach
	public void setupTests() {
		InternalUsersQuantity.setInternalUserQuantity(100);
	}

	@Test
	public void highVolumeTrackLocation() throws InterruptedException {
		ITourGuideService iTourGuideService = new TourGuideService(gpsUtil, iRewardsService);

		List<User> allUsers = new ArrayList<>();
		allUsers = iTourGuideService.getUserList();

		stopWatch.start();
			iTourGuideService.trackEachUserLocation(allUsers);
		stopWatch.stop();

		iTourGuideService.getTracker().stopTracking();
		assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
		System.out.println("highVolumeTrackLocation: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
	}

	@Test
	public void highVolumeGetRewards() throws InterruptedException {
		ITourGuideService iTourGuideService = new TourGuideService(gpsUtil, iRewardsService);

		stopWatch.start();
			Attraction attraction = gpsUtil.getAttractions().get(0);
			List<User> userList = new ArrayList<>();
	
			userList = iTourGuideService.getUserList();
			userList.forEach(user -> user.addVisitedLocation(new VisitedLocation(user.getUserId(), attraction, new Date())));
			userList = iRewardsService.manageEachUserRewardList(userList);
	
			for (User user : userList) {
				assertTrue(user.getUserRewardList().size() > 0);
			}
		stopWatch.stop();

		iTourGuideService.getTracker().stopTracking();
		assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
		System.out.println("highVolumeGetRewards: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
	}

}
