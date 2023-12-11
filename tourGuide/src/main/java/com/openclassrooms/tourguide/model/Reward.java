package com.openclassrooms.tourguide.model;

import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;

/**
 * A model class which creates the POJO (Plain Old Java Object)
 * <code>Reward</code>. It contains getters and setters, as well as an override
 * <code>toSring()</code> method for display in the console.
 *
 * @author [NPC]TourGuide BackEnd Team, Sébastien Cappon
 * @version 1.1
 */
public class Reward {
	public final VisitedLocation visitedLocation;
	public final Attraction attraction;
	private int rewardPoints;

	public Reward(VisitedLocation visitedLocation, Attraction attraction, int rewardPoints) {
		this.visitedLocation = visitedLocation;
		this.attraction = attraction;
		this.rewardPoints = rewardPoints;
	}

	public Reward(VisitedLocation visitedLocation, Attraction attraction) {
		this.visitedLocation = visitedLocation;
		this.attraction = attraction;
	}

	public VisitedLocation getVisitedLocation() {
		return visitedLocation;
	}

	public Attraction getAttraction() {
		return attraction;
	}

	public int getRewardPoints() {
		return rewardPoints;
	}

	public void setRewardPoints(int rewardPoints) {
		this.rewardPoints = rewardPoints;
	}

	@Override
	public String toString() {
		return "[" + visitedLocation + "]" + "[" + attraction + "]" + "[" + rewardPoints + "]";
	}
}