package com.openclassrooms.tourguide.service;

import java.util.List;

import com.openclassrooms.tourguide.dto.AttractionNearUserDto;
import com.openclassrooms.tourguide.model.Reward;
import com.openclassrooms.tourguide.model.User;

import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import tripPricer.Provider;

public interface ITourGuideService {

	public List<User> getAllUsers();
	public User getUser(String userName);
	public VisitedLocation getUserLocation(User user);
	public List<Attraction> getNearByAttractions(VisitedLocation visitedLocation);
	public List<AttractionNearUserDto> formatNearbyAttractions(User user, VisitedLocation visitedLocation, List<Attraction> nearbyAttractions);
	public List<Reward> getUserRewards(User user);
	public List<Provider> getTripDeals(User user);
	
	public void addUser(User user);
	
	public VisitedLocation trackUserLocation(User user);
}