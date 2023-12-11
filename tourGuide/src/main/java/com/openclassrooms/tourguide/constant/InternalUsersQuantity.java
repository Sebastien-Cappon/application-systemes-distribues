package com.openclassrooms.tourguide.constant;

/**
 * A class that initializes a constant that defines the number of users to be
 * internally simulated. This class is only used in the
 * <code>tourGuideService</code>.
 * 
 * @defaultValue 100 for testing.
 * 
 * @author [NPC]TourGuide BackEnd Team
 * @version 1.0
 */
public class InternalUsersQuantity {
	private static int internalUserQuantity = 100;

	public static void setInternalUserQuantity(int internalUserQuantity) {
		InternalUsersQuantity.internalUserQuantity = internalUserQuantity;
	}

	public static int getInternalUserQuantity() {
		return internalUserQuantity;
	}
}