package com.openclassrooms.tourguide.service;

import java.util.List;

import com.openclassrooms.tourguide.model.User;

import gpsUtil.location.Attraction;
import gpsUtil.location.Location;

/**
 * <code>RewardService</code> interface that abstracts it from its
 * implementation in order to achieve better code modularity in compliance with
 * SOLID principles.
 *
 * @author Sébastien Cappon
 * @version 1.0
 */
public interface IRewardService {
	
	public void setDefaultProximityBuffer();
	public void setProximityBuffer(int proximityBuffer);
	public double getDistance(Location firstLocation, Location secondLocation);
	public int getRewardPoints(Attraction attraction, User user);
	
	public boolean isWithinAttractionProximity(Attraction attraction, Location location);
	public User manageUserRewardList(User user);
	public List<User> manageEachUserRewardList(List<User> userList) throws InterruptedException;
}