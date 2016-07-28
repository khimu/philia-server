package com.philia.service;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.skip;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.limit;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.philia.model.Match;
import com.philia.model.Matches;

@Component("matchService")
public class MatchService implements IMatchService {
	
	@Resource
	private MongoOperations mongoTemplate;
	
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
