package com.mss.utils;

/**
 * 
 * @author master software solutions
 * 
 *         This class is responsible of providing key values to game
 * 
 */
public class Session {
	private static double	mGyroValue		= new Utils().random(0.99, 0.99);
	private static double	mAccerlValue	= new Utils().random(1.99, 1.99);
	private static double	mGpsValue	    = new Utils().random(3.99, 14.99);

	public static double getGyroValue() {
		return mGyroValue;
	}

	public static double getGpsValue() {
		return mGpsValue;
	}

	public static void setGpsValue(double mGpsValue) {
		Session.mGpsValue = mGpsValue;
	}

	public static void setGyroValue(double mGyroValue) {
		Session.mGyroValue = mGyroValue;
	}

	public static double getAccerlValue() {
		return mAccerlValue;
	}

	public static void setAccerlValue(double mAccerlValue) {
		Session.mAccerlValue = mAccerlValue;
	}

}
