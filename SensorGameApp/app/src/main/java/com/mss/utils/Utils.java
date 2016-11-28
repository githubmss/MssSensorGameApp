package com.mss.utils;

/**
 * 
 * @author master software solutions
 * 
 * 
 */
public class Utils {

	public double random(double min, double max) {
		double diff = max - min;
		return Math.round((min + Math.random() * diff) * 100.0 / 100.0);
	}
}
