package com.openclassrooms.tourguide.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;

public class ModelTest {

	@Test
	public void preferencesToString_isNotBlank() {
		Preferences preferences = new Preferences(1, 1, 1, 1, 0);
		assertThat(preferences.toString()).isNotBlank();
	}

	@Test
	public void rewardToString_isNotBlank() {
		VisitedLocation visitedLocation = new VisitedLocation(null, null, null);
		Attraction attraction = new Attraction("AttracionName", "City", "State", 0, 0);
		Reward reward = new Reward(visitedLocation, attraction, 10);

		assertThat(reward.toString()).isNotBlank();
	}

	@Test
	public void userToString_isNotBlank() {
		User user = new User(new UUID(0, 0), "userName", "phoneNumber", "emailAddress");
		assertThat(user.toString()).isNotBlank();
	}
}