package com.openclassrooms.tourguide.model;

/**
 * A model class which creates the POJO (Plain Old Java Object)
 * <code>Preferences</code>. It contains getters and setters, as well as an
 * override <code>toSring()</code> method for display in the console.
 *
 * @author [NPC]TourGuide BackEnd Team, Sébastien Cappon
 * @version 1.1
 */
public class Preferences {
	private int attractionProximity = Integer.MAX_VALUE;
	private int tripDuration = 1;
	private int ticketQuantity = 1;
	private int adultQuantity = 1;
	private int childQuantity = 0;

	public Preferences() {

	}
	
	public Preferences(int attractionProximity, int tripDuration, int ticketQuantity, int adultQuantity, int childQuantity) {
		this.attractionProximity = attractionProximity;
		this.tripDuration = tripDuration;
		this.ticketQuantity = ticketQuantity;
		this.adultQuantity = adultQuantity;
		this.childQuantity = childQuantity;
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

	public int getAdultQuantity() {
		return adultQuantity;
	}

	public void setAdultQuantity(int adultQuantity) {
		this.adultQuantity = adultQuantity;
	}

	public int getChildQuantity() {
		return childQuantity;
	}

	public void setChildQuantity(int childQuantity) {
		this.childQuantity = childQuantity;
	}

	@Override
	public String toString() {
		return "[" + attractionProximity + "]" + "[" + tripDuration + "]" + "[" + ticketQuantity + "]" + "["
				+ adultQuantity + "]" + "[" + childQuantity + "]";
	}
}