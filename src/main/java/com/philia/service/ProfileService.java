package com.philia.service;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.philia.app.Constant;
import com.philia.model.Profile;
import com.philia.utils.BitOperations;

@Component("profileService")
public class ProfileService implements IProfileService {
	private final static Logger logger = Logger.getLogger(ProfileService.class);
	
	@Resource
	private MongoOperations mongoTemplate;
	
	public void saveProfile(Profile profile) {
		mongoTemplate.save(profile);
	}
	
	public Profile findByUser(String userId) {
    	return mongoTemplate.findOne(Query.query(Criteria.where("user_id").is(userId)), Profile.class);
	}
	
	// find all matches and create match
	public List<Profile> findAllMatches(int start, Profile newUser) {
		// bisexual - doesnt matter if man or woman
		if(newUser.getGenderInterest() == BitOperations.enable(newUser.getGenderInterest(), Constant.INTEREST_IN_BOTH)) {
			logger.info(newUser.getUserId() + " is interested in both " + newUser.getGenderInterest());
			return mongoTemplate.find(Query.query(
					Criteria.where("user_id").ne(newUser.getUserId())
					.andOperator(
							Criteria.where("interest").is(newUser.getInterest()),
							Criteria.where("dating_intension").is(newUser.getDatingIntension()),
							Criteria.where("gender_interest").is(Constant.INTEREST_IN_MEN)
							.orOperator(
									Criteria.where("gender_interest").is(Constant.INTEREST_IN_WOMEN),
									Criteria.where("gender_interest").is(Constant.INTEREST_IN_BOTH)))
					).skip(start).limit(100), Profile.class);

		// gay man
		} else if(BitOperations.isTrue(newUser.getGenderInterest(), Constant.INTEREST_IN_MEN) == true 
				&& BitOperations.isTrue(newUser.getGender(), Constant.MAN) == true) {
			logger.info(newUser.getUserId() + " is interested in men and is a man " + newUser.getGenderInterest());
			return mongoTemplate.find(
					Query.query(
							Criteria.where("user_id").ne(newUser.getUserId())
							.andOperator(
									Criteria.where("interest").is(newUser.getInterest()),
									Criteria.where("dating_intension").is(newUser.getDatingIntension()),
									Criteria.where("gender_interest").is(Constant.INTEREST_IN_MEN),
									Criteria.where("gender").is(Constant.MAN))							
						)
					.skip(start).limit(100), Profile.class);
		// lesbian 
		} else if (BitOperations.isTrue(newUser.getGenderInterest(), Constant.INTEREST_IN_WOMEN) == true 
				&& BitOperations.isTrue(newUser.getGender(), Constant.WOMAN) == true) {
			logger.info(newUser.getUserId() + " is interested in woman and is a woman " + newUser.getGenderInterest());
			return mongoTemplate.find(
					Query.query(
							Criteria.where("user_id").ne(newUser.getUserId())
							.andOperator(
									Criteria.where("interest").is(newUser.getInterest()),
									Criteria.where("dating_intension").is(newUser.getDatingIntension()),
									Criteria.where("gender_interest").is(Constant.INTEREST_IN_WOMEN),
									Criteria.where("gender").is(Constant.WOMAN))							
						)
					.skip(start).limit(100), Profile.class);
		}
		// straight man - or currently could be lesbian woman
		else if (BitOperations.isTrue(newUser.getGenderInterest(), Constant.INTEREST_IN_WOMEN) == true) {
			logger.info(newUser.getUserId() + " is interested in woman " + newUser.getGenderInterest());
			return mongoTemplate.find(
					Query.query(
							Criteria.where("user_id").ne(newUser.getUserId())							
							.andOperator(
									Criteria.where("interest").is(newUser.getInterest()),
									Criteria.where("dating_intension").is(newUser.getDatingIntension()),
									Criteria.where("gender_interest").is(Constant.INTEREST_IN_WOMEN)
									// assuming MAN
								// ).andOperator(Criteria.where("gender").is(Constant.MAN)
								)							
						)
					.skip(start).limit(100), Profile.class);
		}

		logger.info(newUser.getUserId() + " is interested in woman " + newUser.getGenderInterest());
		
		// straight woman - or currently could be gay man		
		return mongoTemplate.find(
					Query.query(
							Criteria.where("user_id").ne(newUser.getUserId())
							.andOperator(
									Criteria.where("interest").is(newUser.getInterest()),
									Criteria.where("dating_intension").is(newUser.getDatingIntension()),
									Criteria.where("gender_interest").is(Constant.INTEREST_IN_MEN)
									// assuming woman
								// ).andOperator(Criteria.where("gender").is(Constant.WOMAN)
								)							
						)
					.skip(start).limit(100), Profile.class);
	}
}
