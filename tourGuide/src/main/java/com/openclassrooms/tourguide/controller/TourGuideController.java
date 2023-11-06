package com.openclassrooms.tourguide.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;

import com.openclassrooms.tourguide.service.TourGuideService;
import com.openclassrooms.tourguide.user.User;
import com.openclassrooms.tourguide.user.UserReward;

import tripPricer.Provider;

/**
 * A class that receives requests made from some specific URL which parameter is
 * always the user's name. Since <code>@RestController</code> is used, this
 * means the response returned is serialized. The response will be in JSON.
 * 
 * @author [NPC]TourGuide BackEnd Team
 * @version 1.0
 */
@RestController
public class TourGuideController {

	@Autowired
	TourGuideService tourGuideService;

	/**
	 * A <code>RequestMapping</code> method on the <code>/</code> URI with an user
	 * name as <code>RequestParam</code>. It just returns a welcoming message.
	 * 
	 * @return A <code>String</code>.
	 */
	@RequestMapping("/")
	public String index() {
		return "Greetings from TourGuide!";
	}

	/**
	 * A <code>RequestMapping</code> method on the <code>/getLocation</code> URI
	 * with an user name as <code>RequestParam</code>. It calls the
	 * <code>tourGuideService</code> methods <code>getUserLocation(User user)</code>
	 * and returns the <code>VistedLocation</code> for the user whose name is the
	 * one passed in parameter.
	 * 
	 * @singularity VisitedLocation came from GpsUtil external module.
	 * 
	 * @return A <code>VistedLocation</code>.
	 */
	@RequestMapping("/getLocation")
	public VisitedLocation getLocation(@RequestParam String userName) {
		return tourGuideService.getUserLocation(getUser(userName));
	}
	
	/**
	 * Get the closest five tourist attractions to the user - no matter how
	 * far away they are. Return a new JSON object that contains:
	 * - Name of Tourist attraction,
	 * - Tourist attractions lat/long,
	 * - The user's location lat/long
	 * - The distance in miles between the user's location and each of the attractions.
	 * - The reward points for visiting each Attraction.
	 * Note: Attraction reward points can be gathered from RewardsCentral
	 */
	// TODO: Change this method to no longer return a List of Attractions.
	@RequestMapping("/getNearbyAttractions")
	public List<Attraction> getNearbyAttractions(@RequestParam String userName) {
		VisitedLocation visitedLocation = tourGuideService.getUserLocation(getUser(userName));
		return tourGuideService.getNearByAttractions(visitedLocation);
	}

	/**
	 * A <code>RequestMapping</code> method on the <code>/getRewards</code> URI
	 * with an user name as <code>RequestParam</code>. It calls the
	 * <code>tourGuideService</code> methods <code>getUserRewards(User user)</code>
	 * and returns a list of <code>UserReward</code> for the user whose name is the
	 * one passed in parameter.
	 * 
	 * @return A <code>VistedLocation</code>.
	 */
	@RequestMapping("/getRewards")
	public List<UserReward> getRewards(@RequestParam String userName) {
		return tourGuideService.getUserRewards(getUser(userName));
	}

	/**
	 * A <code>RequestMapping</code> method on the <code>/getTripDeals</code> URI
	 * with an user name as <code>RequestParam</code>. It calls the
	 * <code>tourGuideService</code> methods <code>getTripDeals(User user)</code>
	 * and returns a list of <code>Provider</code> for the user whose name is the
	 * one passed in parameter.
	 * 
	 * @singularity Provider came from TripPricer external module.
	 * 
	 * @return A <code>VistedLocation</code>.
	 */
	@RequestMapping("/getTripDeals")
	public List<Provider> getTripDeals(@RequestParam String userName) {
		return tourGuideService.getTripDeals(getUser(userName));
	}

	/**
	 * A with an user name as parameter. It calls the <code>tourGuideService</code>
	 * methods <code>getUser(String userName)</code> and returns a <code>User</code>
	 * whose name is the one passed in parameter.
	 * 
	 * @singularity Provider came from TripPricer external module.
	 * 
	 * @return A <code>VistedLocation</code>.
	 */
	private User getUser(String userName) {
		return tourGuideService.getUser(userName);
	}
}