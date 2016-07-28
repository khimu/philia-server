package com.philia.utils;

/**
 * 
 * @author khimung
 *
 */
public final class BitOperations {

	public static boolean isTrue(int preference, int bit) {
		return (preference & bit) == bit ? true : false;
	}
	
	public static int enable(int preference, int bit) {
		return preference | bit;
	}
	
	public static int disable(int preference, int bit) {
		return preference ^ bit;
	}
}
