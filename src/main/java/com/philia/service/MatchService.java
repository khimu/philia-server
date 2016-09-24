package com.philia.service;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.skip;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.limit;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.philia.model.Conversation;
import com.philia.model.MailBox;
import com.philia.model.Mails;
import com.philia.model.Match;
import com.philia.model.Matches;
import com.philia.model.Message;

@Component("matchService")
public class MatchService implements IMatchService {
	
	@Resource
	private MongoOperations mongoTemplate;
	
	/**
	 * Found a profile that matches the new user's profile and therefore saving a match record for each of the user's that
	 * point to each other as a match
	 * 
	 * @param matchUser			The new user getting created
	 * @param matchMatchUser	The user that matches to the new user
	 * @param userId			The new user's user id
	 * @param matchUserId		The matching user's user id
	 */
	public void saveMatch(Match matchUser, Match matchMatchUser, String userId, String matchUserId) {
				
		/*
		 * save message to mongo
		 */
		mongoTemplate.save(matchUser);
		mongoTemplate.save(matchMatchUser);
		
		boolean userMatchesExist = mongoTemplate.exists(Query.query(Criteria.where("user_id").is(userId)), Matches.class);
		boolean matchMatchesExist = mongoTemplate.exists(Query.query(Criteria.where("user_id").is(matchUserId)), Matches.class);
		   	
    	if(userMatchesExist == false) {
    		Matches box = new Matches();
    		box.setUserId(userId);
    		box.getMatches().add(matchMatchUser);
    		createMatches(box, matchUserId);
    	}
    	else {
    		addMatch(matchUser, matchUserId);
    	}
    	
    	if(matchMatchesExist == false) {
    		Matches box = new Matches();
    		box.setUserId(matchUserId);
    		box.getMatches().add(matchUser);
    		createMatches(box, userId);
    	}
    	else {
    		addMatch(matchMatchUser, userId);
    	}
    	
	}
	
	private void createMatches(Matches matches, String userId) {
    	//build update
    	DBObject dbDoc = new BasicDBObject();
    	mongoTemplate.getConverter().write(matches, dbDoc); //it is the one spring use for convertions.
    	Update update = Update.fromDBObject(dbDoc);

    	mongoTemplate.upsert(Query.query(Criteria.where("user_id").is(userId)), 
    			update, Matches.class);
	}
	
	private void addMatch(Match match, String userId) {
    	mongoTemplate.updateFirst(Query.query(Criteria.where("user_id").is(userId)), 
    			new Update().push("matches", match), Matches.class);
	}
	
	public Matches getMatches(String userId) {
    	return mongoTemplate.findOne(Query.query(Criteria.where("user_id").is(userId)), Matches.class);
	}
	
	
	/**
	 * Support client matches view
	 */
	public List<Match> getNewMatches(String userId, int start) {
		List<Match> matches = mongoTemplate.find(Query.query(Criteria.where("user_id").is(userId)).skip(start).limit(100), Match.class);

		TypedAggregation<Matches> agg = newAggregation(Matches.class, //
				match(Criteria.where("userId").is(userId)),
				unwind("matches"), //
				match(Criteria.where("matches.stage").is(0)),
				project().and("matches.matchedWithUserId").as("matched_with_user_id")
				.and("mails.weight").as("weight")
				.and("mails.created").as("created")
				.and("mails.blurredImage").as("blurred_image")
				.and("mails.clearImage").as("clear_image"),
				skip(start),
				limit(100)				
		);

		AggregationResults<Match> results = mongoTemplate.aggregate(agg, Match.class);

		return results.getMappedResults();
		
		/*
		 * 
		 * db.mailbox.aggregate(
    // Initial document match (uses index, if a suitable one is available)
    { $match: {
        'user_id': "2"
    }},

    // Convert embedded array into stream of documents
    { $unwind: '$mails' },

    // Only match scores of interest from the subarray
    { $match: {
        'mails.deleted' : 0
    }},

    // Note: Could add a `$group` by _id here if multiple matches are expected

    // Final projection: exclude fields with 0, include fields with 1
    { $project: {
        _id: 0,
        conversation_id: "$mails.conversation_id"
    }}
)
		 */
		
		
		 
		//public final static int SEEN = 1 << 0; // 1
	}
	
}
