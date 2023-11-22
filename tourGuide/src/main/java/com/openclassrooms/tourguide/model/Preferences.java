package com.openclassrooms.tourguide.model;

/**
 * A model class which creates the POJO (Plain Old Java Object)
 * <code>UserPreferences</code>. It contains getters and setters.
 *
 * @author [NPC]TourGuide BackEnd Team
 * @version 1.0
 */
public class Preferences {
	private int attractionProximity = Integer.MAX_VALUE;
	private int tripDuration = 1;
	private int ticketQuantity = 1;
	private int numberOfAdults = 1;
	private int numberOfChildren = 0;

	public Preferences() {

	}

	public int getAttractionProximity() {
		return attractionProximity;
	}

	public void setAttractionProximity(int attractionProximity) {
		this.attractionProximity = attractionProximity;
	}

	public int getTripDuration() {
		return tripDuration;
	}

	public void setTripDuration(int tripDuration) {
		this.tripDuration = tripDuration;
	}

	public int getTicketQuantity() {
		return ticketQuantity;
	}

	public void setTicketQuantity(int ticketQuantity) {
		this.ticketQuantity = ticketQuantity;
	}

	public int getNumberOfAdults() {
		return numberOfAdults;
	}

	public void setNumberOfAdults(int numberOfAdults) {
		this.numberOfAdults = numberOfAdults;
	}

	public int getNumberOfChildren() {
		return numberOfChildren;
	}

	public void setNumberOfChildren(int numberOfChildren) {
		this.numberOfChildren = numberOfChildren;
	}

	@Override
	public String toString() {
		return "[" + attractionProximity + "]" + "[" + tripDuration + "]" + "[" + ticketQuantity + "]" + "["
				+ numberOfAdults + "]" + "[" + numberOfChildren + "]";
	}
}