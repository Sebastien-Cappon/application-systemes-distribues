package com.openclassrooms.tourguide.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.openclassrooms.tourguide.service.RewardsService;

import gpsUtil.GpsUtil;
import rewardCentral.RewardCentral;

/**
 * A configuration class that contains 3 Beans for 2 of the modules and a
 * Service used by the application.
 *
 * @author [NPC]TourGuide BackEnd Team
 * 
 * @version 1.0
 */
@Configuration
public class ModuleConfig {

	@Bean
	GpsUtil getGpsUtil() {
		return new GpsUtil();
	}

	@Bean
	RewardsService getRewardsService() {
		return new RewardsService(getGpsUtil(), getRewardCentral());
	}

	@Bean
	RewardCentral getRewardCentral() {
		return new RewardCentral();
	}
}