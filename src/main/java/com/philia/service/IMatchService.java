package com.philia.service;

import java.util.List;

import com.philia.model.Match;

public interface IMatchService {

	public List<Match> getNewMatches(String userId, int start);
	
}
