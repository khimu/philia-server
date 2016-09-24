package com.philia;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import org.junit.Test;

public class ThumbtackTest {

	// This is the text editor interface. 
	// Anything you type or change here will be seen by the other person in real time.
	// To execute Scala, Do not remove the object named Solution that extends App.

	// Stats
	// - insert(value)
	// - mean()
	// - median()

	// value in [0, 1000)
	// insert >> mean, median
	// insert O(1) space
	// median (1, 2, 3, 4, 5) -> 3  (1, 2, 3, 4) -> 2.5

	// 1 3 2 1 0 -> median is 1 (0, 1, 1, 2, 3)

	// 1, 1, 1, 1, 0, 0, 0, 500, 23
	// 0->3
	// 1->4
	// 23->1
	// 500->1
	// [0, 0, 0, ... 0] size = 1000
	// insert(1)
	// [0, 1, 0, ... 0]
	// insert(1)
	// [0, 2, 0, ... 0]


	// [0, 0, 3, 2, 1, 0, 0] = 2
	// 0, 0, 0, 0, 1, 2, 3 -> 0
	// [4, 1, 1, 1, 0 ... 0]

	//   0  1  2  3  4  5  6
	// 333, 1, 45, 1, 5 -> 5 

	// 1, 1, 4, 5, 6
	// target = 3
	// [0, 2, 0, 0, 1, 1, 1, 0...]
	// 0  1  2  3  4  5  6  7

	class Stats {
	    private int[] values = new int[1000];
	    private int insertedCount = 0;
	    
	    public void init() {
	    	for(int i = 0; i < values.length; i ++) {
	    		values[i] = 0;
	    	}
	    }
	    
	    public boolean insert(int value) {
	        int newValue = values[value];
	        newValue ++;
	        values[value] = newValue;
	        System.out.println("Inserting value " + value);
	        insertedCount ++;
	        return true;
	    }
	    
	    public int mean() {
	        int total = 0;
	        int count = 0;
	        
	        for(int i = 0; i < values.length; i ++) {
	            total += values[i] * i;
	            count += values[i];
	        }

	        return total / count;
	    }
	    
	    public int median() {
	        int count = 0;
	        
	        //System.out.println("count is " + count);
	        int mid = insertedCount / 2;
	        //System.out.println("mid is " + mid);

	        int foundValue = 0;
	        count = 0;
	        for(int i = 0; i < values.length; i ++) {
	        	if(values[i] > 0) {
	        		for(int j = 0; j < values[i]; j ++) {
	        			count ++;
	        			//System.out.println("count is " + count);
	        			if(count == mid) {
	        				//System.out.println("count equals mid " + count);
	        				foundValue = i;
	        				//System.out.println("values of index i " + i);
	        			}
	        		}
	        	}
	        }
	        
	        //System.out.println("count is " + count);
	        
	        
	        System.out.println("foundValue " + foundValue);
	        return foundValue;
	    }
	}
	 
	@Test
	public void testThreads() {
		final Stats stat = new Stats();
		stat.init();

		Random random = new Random();
		
		IntStream.range(1, 100).parallel().forEach(i -> stat.insert(random.nextInt(1000)));  

		//System.out.println("");
		System.out.println("median " + stat.median());
		
		
	}
}
