package com.philia.service;

import javax.annotation.Resource;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.philia.model.Profile;

@Component("profileService")
public class ProfileService implements IProfileService {

	@Resource
	private MongoOperations mongoTemplate;
	
	public void saveProfile(Profile profile) {
		mongoTemplate.save(profile);
	}
	
	public Profile findByUser(String userId) {
    	return mongoTemplate.findOne(Query.query(Criteria.where("user_id").is(userId)), Profile.class);
	}
}
