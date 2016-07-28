package com.philia.service;

import com.philia.model.Profile;

public interface IProfileService {

	public void saveProfile(Profile profile);
	
	public Profile findByUser(String userId);
	
}
