package com.openclassrooms.tourguide.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import gpsUtil.GpsUtil;
import rewardCentral.RewardCentral;

/**
 * A configuration class that contains Beans for the modules. The original third
 * bean is no longer useful.
 *
 * @author [NPC]TourGuide BackEnd Team, Sébastien Cappon
 * 
 * @version 1.1
 */
@Configuration
public class ModuleConfig {

	@Bean
	GpsUtil getGpsUtil() {
		return new GpsUtil();
	}

	@Bean
	RewardCentral getRewardCentral() {
		return new RewardCentral();
	}
}