package com.philia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;


public class Apple2 {


	/*

	 1. auto suggestion
	 
	 wor --> word,world
	 
	 String [] dict = new Sting[]{ w o rd, wo rld, book, keyword, mouse}
	 ############
	 
	 root
	 w       | b |  k | m
	 o
	 r
	 d | l
	     d
	 
	 
	 step 1: w root
	 step 2: o is compared with w to see which is greater and o comes before w
	 step 3: o becomes root and w is child of o
	 step 4: 
	 
	 dict.size()= 1000000 --> m log(m)*n
	 
	 linear look up --> O(n) n is max length of matched word
	 
	  wor --> word,world n=5
	  
	 List<String> findAllWords(String prefix, ? dictonary)
	 
	 
	 prefix tree --> trie
	 
	 
	 
	 Set of List<CharTree> where the root contains the first character
	 

	 w
	 
	 word
	 world = 
	 
	 
	 Map<Character, CharTree> root = new HashMap<Character, CharTree>();

	 */
	
	private Map<Character, CharTree> root = new HashMap<Character, CharTree>();
	private String [] dict = new String[]{ "word", "wordy", "world", "book", "keyword", "mouse"};
	
	public void init() {
		for(String word: dict) {
			System.out.println("word " + word);
			
			char[] chars = word.toCharArray();
			
			CharTree tree = root.get(chars[0]);
			
			if(tree == null) {
				tree = new CharTree(chars[0]);
			}
			
			root.put(chars[0], tree);
			
			buildRoot(1, chars, tree);
		}
	}
	
	/*
	 * build up the binary tree of characters that is a multi-leaf tree and not a binary leaf tree
	 */
	public void buildRoot(int index, char[] chars, CharTree tree) {
		if(index < chars.length) {
			// check if the character from the word already exist
			CharTree existing = tree.getChar(chars[index]);
			if(existing == null) {
				// if the character does not exist, create a node to represent the character and add it to the parent node
				existing = new CharTree(chars[index]);
				System.out.println("found new char " + chars[index]);
				
				// add the character node for the character from the word to the parent node
				// if the character node already exist, it will just override it.
				tree.addChar(existing);
			}
			else {
				System.out.println("found existing char " + chars[index]);
			}

			// recursive call to look at the next character in the word and passing it the node representing the current character in the word
			buildRoot(index + 1, chars, existing);
		}
		else {
			System.out.println("found leaf node " + new String(chars));
			
			// found a leaf node
			tree.setWord(new String(chars));
		}
	}

	static class CharTree {
	  char character;
	  String word;
	  
	  // multi-leaf tree and not a binary leaf tree
	  Map<Character, CharTree> nodes = new HashMap<Character, CharTree>();
	  
	  public void setWord(String word) {
		  this.word = word;
	  }
	  
	  public String getWord() {
		  return this.word;
	  }
	  
	  public CharTree(char character) {
	    this.character = character;
	  }
	  
	  public char getCharacter() {
	    return character;
	  }
	  
	  public void addChar(CharTree node) {
		  this.nodes.put(node.getCharacter(), node);
	  }
	  
	  public CharTree getChar(char findChar) {
		  return this.nodes.get(findChar);
	  }
	  
	  public Object[] getNodes() {
		  return this.nodes.values().toArray();
	  }
	  
	}
	
	// performs a search using the multi-tree
	public List<String> findAllWords(String partial) throws Exception {
		char[] chars = partial.toCharArray();
		
		List<String> results = new ArrayList<String>();
		
		CharTree tree = root.get(chars[0]);
		if(tree == null) {
			throw new Exception("No Word found starting with " + chars[0]);
		}

		// traverse to find the last leaf node with the characters from the search word
		CharTree bottom = findWords(1, chars, tree);

		if(bottom != null) {
			// if all characters from the search exist int he tree, a node representing the last character is returned
			System.out.println("bottom: " + bottom.getCharacter());
			// a leaf node is found and the search word exactly matches the leaf node
			//results.add(partial);
			if(bottom.getWord() != null) {
				results.add(bottom.getWord());
			}

			traverseTree(bottom, results);
		}
		else {
			System.out.println("bottom is null");
		}

		return results;
	}
	
	public void traverseTree(CharTree tree, List<String> results) {
		Object[] nodes = tree.getNodes();
		if(nodes != null) {
			for(Object n : nodes) {
				CharTree node = (CharTree)n;
				System.out.println("traverse char " + node.getCharacter());
				
				if(node.getWord() != null) {
					results.add(node.getWord());
				}
				
				traverseTree(node, results);
				System.out.println("return traverse char " + node.getCharacter());
			}
		}
	}
	
	public CharTree findWords(int index, char[] chars, CharTree tree) {
		if(index < chars.length) {
			CharTree existing = tree.getChar(chars[index]);
			if(existing != null) {
				System.out.println("found char " + chars[index]);
				return findWords(index + 1, chars, existing);
			}
			else {
				return null;
			}
		}
		return tree;
	}

	@Test
	public void testInit() throws Exception {
		init();
		List<String> results = findAllWords("wor");
		Assert.assertEquals(3, results.size());
		for(String word : results) {
			System.out.println("result: " + word);
		}
		System.out.println("DONE______________");
		results = findAllWords("worl");
		Assert.assertEquals(1, results.size());
		for(String word : results) {
			System.out.println("result: " + word);
		}
		
		System.out.println("DONE______________");
		results = findAllWords("word");
		Assert.assertEquals(2, results.size());
		for(String word : results) {
			System.out.println("result: " + word);
		}
		
		System.out.println("DONE______________");
		results = findAllWords("wort");
		Assert.assertEquals(0, results.size());
		for(String word : results) {
			System.out.println("result: " + word);
		}
	}

}
