package com.openclassrooms.tourguide.dto;

/**
 * A class which creates the DTO (Data Transfer Object)
 * <code>UserLocationDto</code>. It contains getters and setters, as well as
 * an override <code>toSring()</code> method for display in the console.
 * 
 * @author Sébastien Cappon
 * @version 1.0
 */
public class UserLocationDto {

	private Double latitude;
	private Double longitude;
	
	public UserLocationDto(Double latitude, Double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	
	@Override
	public String toString() {
		return "[" + latitude + "]" + "[" + longitude + "]";
	}
}