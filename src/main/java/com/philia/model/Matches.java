package com.philia.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Create a match entry in mongo so that matches can be retrieved very quickly
 * 
 * @author khimung
 */
@XmlRootElement
@Document(collection = "matches")
public class Matches extends Base {

	private static final long serialVersionUID = 1L;

	/*
	 * Mongo document key for the user id who's match list belongs to
	 */
	@Field(value="user_id")
	private String userId;

	/*
	 * the list of matches for the userId referenced by the document key
	 */
	private List<Match> matches;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public List<Match> getMatches() {
		if(matches == null) {
			matches = new ArrayList<Match>();
		}
		return matches;
	}

	public void setMatches(List<Match> matches) {
		this.matches = matches;
	}

}
