package com.openclassrooms.tourguide.dto;

/**
 * A class which creates the DTO (Data Transfer Object)
 * <code>AttractionNearUserDto</code>. It contains getters and setters, as well
 * as an override <code>toSring()</code> method for display in the console.
 * 
 * @author Sébastien Cappon
 * @version 1.0
 */
public class AttractionNearUserDto {

	private NearbyAttractionDto nearbyAttraction;
	private UserLocationDto userLocation;
	private Double distanceBetween;
	private int rewardPoints;
	
	public AttractionNearUserDto(NearbyAttractionDto nearbyAttraction, UserLocationDto userLocation, Double distanceBetween, int rewardPoints) {
		this.nearbyAttraction = nearbyAttraction;
		this.userLocation = userLocation;
		this.distanceBetween = distanceBetween;
		this.rewardPoints = rewardPoints;
	}

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

	@Override
	public String toString() {
		return "[" + nearbyAttraction.getName() + ":" + nearbyAttraction.getLatitude() + ":"
				+ nearbyAttraction.getLongitude() + "]" + "[" + userLocation.getLatitude() + ":"
				+ userLocation.getLongitude() + "]" + "[" + distanceBetween + "]" + "[" + rewardPoints + "]";
	}
}