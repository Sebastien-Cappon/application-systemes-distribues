package com.openclassrooms.tourguide.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class DtoTest {
	
	private NearbyAttractionDto nearbyAttractionDto = new NearbyAttractionDto("Name", 0d, 0d);
	private UserLocationDto userLocationDto = new UserLocationDto(1d, 1d);
	
	@Test
	public void attractionNearUserDtoToString_shouldReturnOk() {
		AttractionNearUserDto attractionNearUserDto = new AttractionNearUserDto(nearbyAttractionDto, userLocationDto, 1d, 10);
		assertThat(attractionNearUserDto.toString()).isNotBlank();
	}

	@Test
	public void nearbyAttractionDtoToString_shouldReturnOk() {
		assertThat(nearbyAttractionDto.toString()).isNotBlank();
	}

	@Test
	public void userLocationDtoToString_shouldReturnOk() {
		assertThat(userLocationDto.toString()).isNotBlank();
	}
}