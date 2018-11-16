package com.philia;

import org.junit.Test;

public class BitMaskTest {
	
	private int preference = 0;
	
	private int married = 1;
	private int gender = 2;
	private int work = 4;
	private int college = 8;
	private int religious = 16;
	
	@Test
	public void testTh() {
		System.out.println( (1%2) );
	}
	
	@Test
	public void testBitAnd() {
		// enable the preference
		preference = married | preference;
		System.out.println( preference);
		preference = preference | work;
		System.out.println( preference);
		preference = preference | college;
		System.out.println( preference);
		
		preference = preference << married;
		System.out.println( preference);
		
		
		/*
		 * check if preference is enabled 
		 */
		int outcome = preference & married;
		System.out.println(outcome + " " + preference);
		outcome = preference & work;
		System.out.println(outcome + " " + preference);
		outcome = preference & college;
		System.out.println(outcome + " " + preference);
	}

	
	//byte b = 0;
//  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  <= bit value
// 15 14 13 12 11 10  9  8  7  6  5  4  3  2  1  0  <= bit number
 
//b = 1;
//  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  1  <= bit value
// 15 14 13 12 11 10  9  8  7  6  5  4  3  2  1  0  <= bit number
 
//b = 1 << 5;
//  0  0  0  0  0  0  0  0  0  0  1  0  0  0  0  0  <= bit value
// 15 14 13 12 11 10  9  8  7  6  5  4  3  2  1  0  <= bit number
	private final static int MARRIAGE = 1 << 0; // 1
	private final static int HANGOUT = 1 << 1; // 2
	private final static int CURIOUS = 1 << 2; // 4

	private final static int INTEREST_IN_MEN = 1 << 3; // 8
	private final static int INTEREST_IN_WOMEN = 1 << 4; // 16
	
	/*
	Dec     Hex     Shift
	0       0x0     0
	1       0x1     1 << 0 (or simply 1)
	2       0x2     1 << 1
	4       0x4     1 << 2
	8       0x8     1 << 3
	16      0x10    1 << 4
	32      0x20    1 << 5
	64      0x40    1 << 6
	128     0x80    1 << 7
	256     0x100   1 << 8
	*/
	
	//--- Masks ---//
	
	@Test
	public void testInterest() {
		 Integer nature = 1 << 0; // 1
		 Integer culture = 1 << 1; // 2
		 Integer ambition = 1 << 2; // 4
		 Integer career = 1 << 3; // 8
		 Integer travel = 1 << 4; // 16
		 Integer education = 1 << 5; // 32
		 Integer fashion = 1 << 6; // 64
		 Integer ethics = 1 << 7; // 128
		 Integer religion = 1 << 8; // 256
		 Integer power = 1 << 9; // 512
		 Integer wealth = 1 << 10; // 1024
		 Integer drive = 1 << 11; // 2048
		 Integer beauty = 1 << 12; // 4096
		 Integer family = 1 << 13; // 8192
		 Integer fitness = 1 << 14; // 16384
		 Integer food = 1 << 15; // 32768
		 Integer books = 1 << 16; // 65536
		 Integer artistry = 1 << 17; // 131072
		 
			int interest = career | fashion | education | power | wealth | beauty | family;
			System.out.println(interest);
	}
	 
	@Test
	public void testFlag() {
		int vote = 1 << 0; //1
		System.out.println(vote);
		int add = 1 << 1; //2
		System.out.println(add);
		int remove = 1 << 2; //4
		System.out.println(remove);
		int manage = 1 << 3; //8
		System.out.println(manage);
		int comment = 1 << 4; //16
		System.out.println(comment);
		int publish = 1 << 25; //33554432
		System.out.println(publish);
		 
		//--- Sets ---//
		 
		int user1 = vote | remove | manage; //can "vote", "remove", "manage"
		System.out.println(user1);
		int user2 = user1 | publish; //all `user1` plus "publish"
		System.out.println(user2);
		int user3 = user2 ^ manage; //all `user2` minus "manage"
		System.out.println(user3);
		 
		//--- Matches ---//
		 
		System.out.println("USER 1 : " + user1); //13
		 
		System.out.println(user1 & vote); //1 : TRUE
		System.out.println(user1 & add); //0 : FALSE
		System.out.println(user1 & remove); //4 : TRUE
		System.out.println(user1 & manage); //8 : TRUE
		System.out.println(user1 & comment); //0 : FALSE
		System.out.println(user1 & publish); //0 : FALSE
		 
		System.out.println("USER 2 : "+ user2); //33554445
		 
		System.out.println(user2 & vote); //1 : TRUE
		System.out.println(user2 & add); //0 : FALSE
		System.out.println(user2 & remove); //4 : TRUE
		System.out.println(user2 & manage); //8 : TRUE
		System.out.println(user2 & comment); //0 : FALSE
		System.out.println(user2 & publish); //33554432 : TRUE
		 
		System.out.println("USER 3 : "+ user3); //33554437
		 
		System.out.println(user3 & vote); //1 : TRUE
		System.out.println(user3 & add); //0 : FALSE
		System.out.println(user3 & remove); //4 : TRUE
		System.out.println(user3 & manage); //0 : FALSE
		System.out.println(user3 & comment); //0 : FALSE
		System.out.println(user3 & publish); //33554432 : TRUE
	}

}
