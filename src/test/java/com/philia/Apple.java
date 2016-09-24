package com.philia;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.junit.Test;

public class Apple {
	
	char[] apple = new char[]{'a', 'b', 'c'};
	int[] weights = new int[]{10, 20, 70};

	@Test
	public void testThis() {
		Map<Integer, Integer> weightsToElements = new HashMap<Integer, Integer>();
		
		int percentage = 0;
		for(int j = 0; j < weights.length; j ++) {
			for(int k = 0; k < weights[j]; k ++) {
				weightsToElements.put(percentage, j);
				percentage ++;
				System.out.println("j is " + j + " and i is " + percentage);
			}
		}

		
		Random r = new Random();
		
		for(int i = 0; i < 100; i ++) {
			int index = r.nextInt(100);

			System.out.println("value should be c since it has the highest probability : " + apple[weightsToElements.get(index)]);
		}
	}
}
