package com.openclassrooms.tourguide.constant;

/**
 * A class that initializes a constant that defines the number of users to be
 * simulated in the internal tests. This class is only used in the
 * <code>tourGuideService</code>, for testing purpose with internal users.
 * 
 * @defaultValue 100,000 for testing.
 * 
 * @author [NPC]TourGuide BackEnd Team
 * @version 1.0
 */
public class InternalUsersQuantity {
	private static int internalUserNumber = 100;

	public static void setInternalUserNumber(int internalUserNumber) {
		InternalUsersQuantity.internalUserNumber = internalUserNumber;
	}

	public static int getInternalUserNumber() {
		return internalUserNumber;
	}
}