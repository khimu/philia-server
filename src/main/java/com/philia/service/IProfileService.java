package com.philia.service;

import java.util.List;

import com.philia.model.Profile;

public interface IProfileService {

	public void saveProfile(Profile profile);
	
	public Profile findByUser(String userId);
	
	public List<Profile> findAllMatches(int start, Profile newUser);
	
}
