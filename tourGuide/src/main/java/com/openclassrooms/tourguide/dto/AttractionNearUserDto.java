package com.openclassrooms.tourguide.dto;

public class AttractionNearUserDto {

	private NearbyAttractionDto nearbyAttraction;
	private UserLocationDto userLocation;
	private Double distanceBetween;
	private int rewardPoints;

	public NearbyAttractionDto getNearbyAttraction() {
		return nearbyAttraction;
	}

	public void setNearbyAttraction(NearbyAttractionDto nearbyAttraction) {
		this.nearbyAttraction = nearbyAttraction;
	}

	public UserLocationDto getUserLocation() {
		return userLocation;
	}

	public void setUserLocation(UserLocationDto userLocation) {
		this.userLocation = userLocation;
	}

	public Double getDistanceBetween() {
		return distanceBetween;
	}

	public void setDistanceBetween(Double distanceBetween) {
		this.distanceBetween = distanceBetween;
	}

	public int getRewardPoints() {
		return rewardPoints;
	}

	public void setRewardPoints(int rewardPoints) {
		this.rewardPoints = rewardPoints;
	}
}