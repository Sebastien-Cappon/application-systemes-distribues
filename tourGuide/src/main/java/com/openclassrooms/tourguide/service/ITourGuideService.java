package com.openclassrooms.tourguide.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.openclassrooms.tourguide.dto.AttractionNearUserDto;
import com.openclassrooms.tourguide.model.Reward;
import com.openclassrooms.tourguide.model.User;
import com.openclassrooms.tourguide.util.UsersTracker;

import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import tripPricer.Provider;

/**
 * <code>TourGuideService</code> interface that abstracts it from its
 * implementation in order to achieve better code modularity in compliance with
 * SOLID principles.
 *
 * @author Sébastien Cappon
 * @version 1.0
 */
public interface ITourGuideService {
	public UsersTracker getTracker(); 

	public List<User> getUserList();
	public User getUserByName(String username);
	public VisitedLocation getUserLocation(User user);
	public List<Attraction> getNearbyAttractionList(VisitedLocation visitedLocation);
	public List<Reward> getUserRewardList(User user);
	public List<Provider> getTripDealList(User user);
	
	public void addUser(User user);
	
	public VisitedLocation trackUserLocation(User user);
	public Map<UUID, VisitedLocation> trackEachUserLocation(List<User> userList) throws InterruptedException;
	public List<AttractionNearUserDto> formatNearbyAttractionList(User user, VisitedLocation visitedLocation, List<Attraction> nearbyAttractionList);
}