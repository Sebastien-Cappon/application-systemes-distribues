package com.openclassrooms.tourguide.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import gpsUtil.location.VisitedLocation;
import tripPricer.Provider;

/**
 * A model class which creates the POJO (Plain Old Java Object)
 * <code>User</code>. It contains getters and setters.
 *
 * @author [NPC]TourGuide BackEnd Team
 * @version 1.0
 */
public class User {
	private final UUID userId;
	private final String userName;

	private String phoneNumber;
	private String emailAddress;
	private Date latestLocationTimestamp;

	private List<VisitedLocation> visitedLocations = new ArrayList<>();
	private List<Reward> userRewards = new ArrayList<>();
	private Preferences userPreferences = new Preferences();
	private List<Provider> tripDeals = new ArrayList<>();

	public User(UUID userId, String userName, String phoneNumber, String emailAddress) {
		this.userId = userId;
		this.userName = userName;
		this.phoneNumber = phoneNumber;
		this.emailAddress = emailAddress;
	}

	public UUID getUserId() {
		return userId;
	}

	public String getUserName() {
		return userName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public Date getLatestLocationTimestamp() {
		return latestLocationTimestamp;
	}

	public void setLatestLocationTimestamp(Date latestLocationTimestamp) {
		this.latestLocationTimestamp = latestLocationTimestamp;
	}

	public List<VisitedLocation> getVisitedLocations() {
		return visitedLocations;
	}

	public void setVisitedLocations(List<VisitedLocation> visitedLocations) {
		this.visitedLocations = visitedLocations;
	}

	public List<Reward> getUserRewards() {
		return userRewards;
	}

	public void setUserRewards(List<Reward> userRewards) {
		this.userRewards = userRewards;
	}

	public Preferences getUserPreferences() {
		return userPreferences;
	}

	public void setUserPreferences(Preferences userPreferences) {
		this.userPreferences = userPreferences;
	}

	public List<Provider> getTripDeals() {
		return tripDeals;
	}

	public void setTripDeals(List<Provider> tripDeals) {
		this.tripDeals = tripDeals;
	}

	public VisitedLocation getLastVisitedLocation() {
		return this.getVisitedLocations().get(this.getVisitedLocations().size() - 1);
	}

	public void addToVisitedLocations(VisitedLocation visitedLocation) {
		this.getVisitedLocations().add(visitedLocation);
	}

	public void addUserReward(Reward userReward) {
		if (this.getUserRewards().stream().filter(r -> !r.attraction.attractionName.equals(userReward.attraction)).count() == 0) {
			this.getUserRewards().add(userReward);
		}
	}

	public void clearVisitedLocations() {
		this.getVisitedLocations().clear();
	}

	@Override
	public String toString() {
		return "[" + userId + "]" + "[" + userName + "]" + "[" + phoneNumber + "]" + "[" + emailAddress + "]" + "["
				+ latestLocationTimestamp + "]" + "[" + visitedLocations + "]" + "[" + userRewards + "]" + "["
				+ userPreferences + "]" + "[" + tripDeals + "]";
	}
}