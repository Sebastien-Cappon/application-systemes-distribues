package com.openclassrooms.tourguide.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import gpsUtil.location.VisitedLocation;
import tripPricer.Provider;

/**
 * A model class which creates the POJO (Plain Old Java Object)
 * <code>User</code>. It contains getters and setters, as well as an override
 * <code>toSring()</code> method for display in the console.
 * 
 * @singularity <code>addUserReward</code> method has been modified to manage
 *              the multithreading induced by the <code>getAttraction()</code>
 *              method of the <code>GpsUtil</code> module, without creating a
 *              list of temporary rewards, when it is called in the
 *              <code>calculateRewards()</code> method of the
 *              <code>RewardService</code> class.
 * 
 * @author [NPC]TourGuide BackEnd Team, Sébastien Cappon
 * @version 1.1
 */
public class User {
	private final UUID userId;
	private final String name;

	private String phoneNumber;
	private String emailAddress;
	private Date latestLocationTimestamp;

	private List<VisitedLocation> visitedLocationList = new ArrayList<>();
	private List<Reward> userRewardList = new ArrayList<>();
	private Preferences userPreferences = new Preferences();
	private List<Provider> tripDealList = new ArrayList<>();

	public User(UUID userId, String name, String phoneNumber, String emailAddress) {
		this.userId = userId;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.emailAddress = emailAddress;
	}

	public UUID getUserId() {
		return userId;
	}

	public String getName() {
		return name;
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

	public List<VisitedLocation> getVisitedLocationList() {
		return visitedLocationList;
	}

	public void setVisitedLocationList(List<VisitedLocation> visitedLocationList) {
		this.visitedLocationList = visitedLocationList;
	}

	public List<Reward> getUserRewardList() {
		return userRewardList;
	}

	public void setUserRewardList(List<Reward> userRewardList) {
		this.userRewardList = userRewardList;
	}

	public Preferences getUserPreferences() {
		return userPreferences;
	}

	public void setUserPreferences(Preferences userPreferences) {
		this.userPreferences = userPreferences;
	}

	public List<Provider> getTripDealList() {
		return tripDealList;
	}

	public void setTripDealList(List<Provider> tripDealList) {
		this.tripDealList = tripDealList;
	}

	public VisitedLocation getLastVisitedLocation() {
		return this.getVisitedLocationList().get(this.getVisitedLocationList().size() - 1);
	}

	public void addVisitedLocation(VisitedLocation visitedLocation) {
		this.getVisitedLocationList().add(visitedLocation);
	}

	public void addReward(Reward newReward) {
		List<Reward> userRewardTempList = new ArrayList<>(getUserRewardList());

		if (userRewardTempList.stream().filter(reward -> reward.attraction.attractionName.equals(newReward.attraction.attractionName)).count() == 0) {
			userRewardList.add(newReward);
		}
	}

	@Override
	public String toString() {
		return "[" + userId + "]" + "[" + name + "]" + "[" + phoneNumber + "]" + "[" + emailAddress + "]" + "["
				+ latestLocationTimestamp + "]" + "[" + visitedLocationList + "]" + "[" + userRewardList + "]" + "["
				+ userPreferences + "]" + "[" + tripDealList + "]";
	}
}