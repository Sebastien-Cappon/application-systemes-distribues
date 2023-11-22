package com.openclassrooms.tourguide.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.tourguide.dto.AttractionNearUserDto;
import com.openclassrooms.tourguide.model.Reward;
import com.openclassrooms.tourguide.model.User;
import com.openclassrooms.tourguide.service.ITourGuideService;

import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
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
	private ITourGuideService iTourGuideService;

	/**
	 * A <code>RequestMapping</code> method on the <code>/</code> URI with an user
	 * name as <code>RequestParam</code>. It just returns a welcoming message.
	 * 
	 * @return A <code>String</code>.
	 */
	@GetMapping("/")
	public String getHomePage() {
		return "Greetings from TourGuide!";
	}

	@GetMapping("/user")
	public User getUser(@RequestParam String userName) {
		return iTourGuideService.getUser(userName);
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
	@GetMapping("/getLocation")
	public VisitedLocation getUserLocation(@RequestParam String userName) {
		return iTourGuideService.getUserLocation(iTourGuideService.getUser(userName));
	}
	
	/**
	 * Get the closest five tourist attractions to the user no matter how far away
	 * they are. It returns a new JSON object that contains the name and the lat/long of the
	 *         tourist attraction, the user's location lat/long, the distance in
	 *         miles between the user's location and each of the attractions. and
	 *         the reward points for visiting each Attraction.
	 * 
	 * @return A list of <code>AttractionNearUserDto</code>.
	 */
	@GetMapping("/getNearbyAttractions")
	public List<AttractionNearUserDto> getNearbyAttractions(@RequestParam String userName) {
		User user = iTourGuideService.getUser(userName);
		VisitedLocation visitedLocation = iTourGuideService.getUserLocation(user);
		List<Attraction> nearbyAttraction = iTourGuideService.getNearByAttractions(visitedLocation);
		
		return iTourGuideService.formatNearbyAttractions(user, visitedLocation, nearbyAttraction);
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
	@GetMapping("/getRewards")
	public List<Reward> getUserRewards(@RequestParam String userName) {
		return iTourGuideService.getUserRewards(iTourGuideService.getUser(userName));
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
	@GetMapping("/getTripDeals")
	public List<Provider> getTripDeals(@RequestParam String userName) {
		return iTourGuideService.getTripDeals(iTourGuideService.getUser(userName));
	}
}