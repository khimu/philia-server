package com.philia;

import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

public class OpenX {
	
	public void parseFrame() {
		// hex 0x00-00-01 = starter code
		// hex 0x00-00-00-01 = starter code
		// frame = everything between starter code
		//  ie 0x000001abefdfgf0000013498afagabe000001
		// frames = 4, 6
		byte[] streamBytes;
		
	}
	
	@Test
	public void findSubString() {
		String string = "12xy345xy2312xeyee123xy23yx33";
		String search = "xy";
		
		int indexOfSearchString = 0;
		char[] originalString = string.toCharArray();
		char[] searchString = search.toCharArray();
		char[] results = new char[originalString.length];
		
		for(int i = 0; i < originalString.length; i ++) {
			if(originalString[i] == searchString[indexOfSearchString]) {
				boolean found = true;
				do {
					if(originalString[i] != searchString[indexOfSearchString]) {
						found = false;
					}
					indexOfSearchString ++;
					i ++;					
				}while(indexOfSearchString < searchString.length && i < originalString.length && found == true);

				
				if(found == true) {
					for(int j = 0; j < searchString.length; j ++) {
						results[i - searchString.length + j] = searchString[j];
					}
				}
				else {
					for(int j = 0; j < indexOfSearchString; j ++) {
						results[i] = '+';
					}
				}
				indexOfSearchString = 0;	
			}
			else {
				results[i] = '+';
			}
		}
		
		for(int i = 0; i < originalString.length; i ++) {
			System.out.print(results[i]);
		}
		
		Assert.assertEquals(results.length, originalString.length);
		
	}
	
	// use rand5 to get random number between 1 - 7 with even probability and distribution
	private int rand5() {
		Random random = new Random();
		return random.nextInt(5);
	}
	
	public void testRand7() {
		
	}

}
