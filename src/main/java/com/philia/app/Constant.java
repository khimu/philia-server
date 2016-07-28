package com.philia.app;

public class Constant {

	/*
	 * Member's preference
	 * 
	 * Search for match on datingIntension where ...
	 */
	public final static int MARRIAGE = 1 << 0; // 1
	public final static int HANGOUT = 1 << 1; // 2
	public final static int CURIOUS = 1 << 2; // 4

	/*
	 * Member's preference
	 * 
	 * Search for match on genderInterest where genderInterest = 8 or genderInterest = 16 or genderInterest = 24
	 */
	public final static int INTEREST_IN_MEN = 1 << 0; // 1
	public final static int INTEREST_IN_WOMEN = 1 << 1; // 2
	public final static int INTEREST_IN_BOTH = 1 << 2; // 4 to indicate like of both gender

	/*
	 * Member's gender
	 */
	public final static int MAN = 0;
	public final static int WOMAN = 1;
	
	public final static int SEEN = 1 << 0; // 1
	public final static int LIKE = 1 << 1; // 2 
	public final static int DISLIKE = 1 << 2; // 4
	public final static int TALKED = 1 << 3; // 8
	public final static int MET = 1 << 4; // 16
	
	
	public static class Interest {
		public Integer nature = 1 << 0; // 1
		public Integer culture = 1 << 1; // 2
		public Integer ambition = 1 << 2; // 4
		public Integer career = 1 << 3; // 8
		public Integer travel = 1 << 4; // 16
		public Integer education = 1 << 5; // 32
		public Integer fashion = 1 << 6; // 64
		public Integer ethics = 1 << 7; // 128
		public Integer religion = 1 << 8; // 256
		public Integer power = 1 << 9; // 512
		public Integer wealth = 1 << 10; // 1024
		public Integer drive = 1 << 11; // 2048
		public Integer beauty = 1 << 12; // 4096
		public Integer family = 1 << 13; // 8192
		public Integer fitness = 1 << 14; // 16384
		public Integer food = 1 << 15; // 32768
		public Integer books = 1 << 16; // 65536
		public Integer artistry = 1 << 17; // 131072
	}

}
