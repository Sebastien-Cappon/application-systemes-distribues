package com.openclassrooms.tourguide.service;

import com.openclassrooms.tourguide.model.User;

import gpsUtil.location.Attraction;
import gpsUtil.location.Location;

public interface IRewardService {
	
	public double getDistance(Location loc1, Location loc2);

	public boolean isWithinAttractionProximity(Attraction attraction, Location location);
	public User calculateRewards(User user);
}