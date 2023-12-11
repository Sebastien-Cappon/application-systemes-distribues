package com.openclassrooms.tourguide.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.openclassrooms.tourguide.service.ITourGuideService;

@WebMvcTest(controllers = TourGuideController.class)
public class TourGuideControllerTest {

	@Autowired
	MockMvc mockMvc;
	
	@MockBean
	ITourGuideService iTourGuideService;

	@Test
	public void getHomePage_shouldReturnOk() throws Exception {
		mockMvc.perform(get("/")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
		    .andExpect(content().string("Greetings from TourGuide!"));
	}
	
	@Test
	public void getUser_shouldReturnOk() throws Exception {
			mockMvc.perform(get("/user")
				.accept(MediaType.APPLICATION_JSON)
				.param("userName", "internalUser0"))
			.andExpect(status().isOk());
	}
	
	@Test
	public void getLocation_shouldReturnOk() throws Exception {
			mockMvc.perform(get("/getLocation")
				.accept(MediaType.APPLICATION_JSON)
				.param("userName", "internalUser0"))
			.andExpect(status().isOk());
	}
	
	@Test
	public void getNearbyAttractionList_shouldReturnOk() throws Exception {
		mockMvc.perform(get("/getNearbyAttractions")
				.accept(MediaType.APPLICATION_JSON)
				.param("userName", "internalUser0"))
		.andExpect(status().isOk());
	}
	
	@Test
	public void getUserRewardList_shouldReturnOk() throws Exception {
		mockMvc.perform(get("/getRewards")
				.accept(MediaType.APPLICATION_JSON)
				.param("userName", "internalUser0"))
		.andExpect(status().isOk());
	}
	
	@Test
	public void getTripDealList_shouldReturnOk() throws Exception {
			mockMvc.perform(get("/getTripDeals")
				.accept(MediaType.APPLICATION_JSON)
				.param("userName", "internalUser0"))
			.andExpect(status().isOk());
	}
}