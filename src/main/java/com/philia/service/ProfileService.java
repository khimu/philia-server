package com.philia.service;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.limit;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.skip;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.philia.app.Constant;
import com.philia.model.Match;
import com.philia.model.Matches;
import com.philia.model.Profile;
import com.philia.utils.BitOperations;

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
	
	// find all matches and create match
	public List<Profile> findAllMatches(int start, Profile newUser) {
		// bisexual - doesnt matter if man or woman
		if(newUser.getGenderInterest() == BitOperations.enable(newUser.getGenderInterest(), Constant.INTEREST_IN_BOTH)) {
			return mongoTemplate.find(Query.query(
					Criteria.where("interest").is(newUser.getInterest())
					.andOperator(Criteria.where("dating_intension").is(newUser.getDatingIntension()))
					.andOperator(
							Criteria.where("gender_interest").is(Constant.INTEREST_IN_MEN)
							.orOperator(Criteria.where("gender_interest").is(Constant.INTEREST_IN_WOMEN)
									.orOperator(Criteria.where("gender_interest").is(Constant.INTEREST_IN_BOTH))))
					).skip(start).limit(100), Profile.class);

		// gay man
		} else if(BitOperations.isTrue(newUser.getGenderInterest(), Constant.INTEREST_IN_MEN) == true 
				&& BitOperations.isTrue(newUser.getGender(), Constant.MAN) == true) {
			return mongoTemplate.find(
					Query.query(
							Criteria.where("interest").is(newUser.getInterest())
							.andOperator(Criteria.where("dating_intension").is(newUser.getDatingIntension()))
							.andOperator(
									Criteria.where("gender_interest").is(Constant.INTEREST_IN_MEN)
								).andOperator(Criteria.where("gender").is(Constant.MAN))							
						)
					.skip(start).limit(100), Profile.class);
		// lesbian 
		} else if (BitOperations.isTrue(newUser.getGenderInterest(), Constant.INTEREST_IN_WOMEN) == true 
				&& BitOperations.isTrue(newUser.getGender(), Constant.WOMAN) == true) {
			return mongoTemplate.find(
					Query.query(
							Criteria.where("interest").is(newUser.getInterest())
							.andOperator(Criteria.where("dating_intension").is(newUser.getDatingIntension()))
							.andOperator(
									Criteria.where("gender_interest").is(Constant.INTEREST_IN_WOMEN)
								).andOperator(Criteria.where("gender").is(Constant.WOMAN))							
						)
					.skip(start).limit(100), Profile.class);
		}
		// straight man - or currently could be lesbian woman
		else if (BitOperations.isTrue(newUser.getGenderInterest(), Constant.INTEREST_IN_WOMEN) == true) {
			return mongoTemplate.find(
					Query.query(
							Criteria.where("interest").is(newUser.getInterest())
							.andOperator(Criteria.where("dating_intension").is(newUser.getDatingIntension()))
							.andOperator(
									Criteria.where("gender_interest").is(Constant.INTEREST_IN_WOMEN)
									// assuming MAN
								// ).andOperator(Criteria.where("gender").is(Constant.MAN)
								)							
						)
					.skip(start).limit(100), Profile.class);
		}
		
		// straight woman - or currently could be gay man		
		return mongoTemplate.find(
					Query.query(
							Criteria.where("interest").is(newUser.getInterest())
							.andOperator(Criteria.where("dating_intension").is(newUser.getDatingIntension()))
							.andOperator(
									Criteria.where("gender_interest").is(Constant.INTEREST_IN_MEN)
									// assuming woman
								// ).andOperator(Criteria.where("gender").is(Constant.WOMAN)
								)							
						)
					.skip(start).limit(100), Profile.class);
	}
}
