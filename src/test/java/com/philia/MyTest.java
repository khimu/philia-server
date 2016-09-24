package com.philia;

import java.util.ArrayDeque;
import java.util.Deque;

public class MyTest {

	    public void testSolution(String S, int K) {
	        // write your code in Java SE 8
	        int requiresRotation = K;
	        int pairs = 0;
	        
	        Deque<Character> bad = new ArrayDeque<Character>();
	        Deque<Character> good = new ArrayDeque<Character>();
	        
	        char[] brackets = S.toCharArray();
	        
	        StringBuilder b = new StringBuilder();
	        
	        for(char c: brackets) {
	            if(c == '(') {
	                if(bad.size() > 0) { 
	                    bad.pop();
	                    pairs ++;
	                    if(requiresRotation < K) {
	                    	requiresRotation ++;
	                    }
	                    
	                }
	                else {
	                    good.push(c);
	                }
	            }
	            if(c == ')') {
	                if(good.size() > 0) {
	                    good.pop();
	                    pairs ++;
	                    System.out.println("()");
	                }
	                else {
	                    bad.push(c);
	                    if(requiresRotation > 0) {
	                    	requiresRotation --;
	                    }
	                    else {
	                    	System.out.println("(");
	                    }
	                    
	                }
	            }
	        }
	        
	        if(K > (requiresRotation * 2)) {
	            System.out.println((pairs * 2) + (requiresRotation * 2));
	        }
	        else {
	        	System.out.println((pairs * 2) + (K * 2));
	        }	        
	    }
}
