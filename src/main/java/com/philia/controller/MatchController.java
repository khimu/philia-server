package com.philia.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.philia.model.Match;
import com.philia.service.IMatchService;

/**
 * 
 * @author khimung
 *
 */
@RestController
public class MatchController {
	
	@Resource
	private IMatchService matchService;
	

	/**
	 * {@link /matches/{userId}}
	 * 
	 * @return
	 */
	@RequestMapping(value = "/matches/{userId}", method = RequestMethod.GET)
	public List<Match> matches(@PathVariable("userId") String userId, @RequestParam(value = "start", required = false, defaultValue = "0") int start) {
		// mark all match record as seen asynchronously upon retrieval of list
		// Find all users in DB given list of user IDS
		return matchService.getNewMatches(userId, start);
	}
}
