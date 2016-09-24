package com.philia;

import org.junit.Assert;
import org.junit.Test;

public class InterviewTest {
	
	@Test
	public void testThis() {
		
		MyChar charOne = new MyChar("TOP");
		MyChar charTwo = new MyChar("POT");
		
		if(charOne.equals(charTwo)) {
			System.out.println("equals");
		}
		else {
			System.out.println("not equals");
		}
		
		Assert.assertTrue(charOne.equals(charTwo));
		
		MyChar charThree = new MyChar("CAS");

		Assert.assertFalse(charOne.equals(charThree));
		
		MyChar charFour = new MyChar("SCA");
		
		Assert.assertTrue(charThree.equals(charFour));
		
		MyChar charFive = new MyChar("bce");
		
		Assert.assertFalse(charFive.equals(charFour));
		
		MyChar charSix = new MyChar("abc");
		
		Assert.assertFalse(charFive.equals(charSix));
		
		MyChar charSeven = new MyChar("A");
		System.out.println("charSeven:  " + charSeven.hashCode());
	}
	
	class MyChar {
		String theChar;
		
		public MyChar(String theChar) {
			this.theChar = theChar;
		}
		
		@Override
		public int hashCode() {
			return theChar.length() * bytearray2intarray(this.theChar.getBytes());
		}
		
		@Override
		public boolean equals(Object other) {
			return this.hashCode() == other.hashCode();
		}
	}

	public int bytearray2intarray(byte[] barray)
	 {
	   int i = 0;
	   for (byte b : barray) {
	       i += b & 0xff;
	       System.out.println("byte: " + (b & 0xff));
	   }
	   System.out.println("i: " +i);
	   return i;
	 }

}
