package com.philia;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.catalina.Store;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.BulkWriteOperation;
import com.mongodb.BulkWriteResult;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.philia.model.MailBox;
import com.philia.model.Mails;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:test-context.xml", "classpath:/spring/database.xml"})
public class MongoTest {
	
	@Resource
	private MongoTemplate mongoTemplate;
	
	@Test
	public void testFilterSubArrayList() {
	MailBox mailbox0 = new MailBox();
	   	
    	Mails mail0 = new Mails();
    	mail0.setConversationId("3");
    	mail0.setUserId("1");
    	mail0.setUnread(0);
    	mail0.setFromFirstName("John");
    	
    	mailbox0.setUserId("1");
    	mailbox0.getMails().add(mail0);
    	
    	//build update
    	DBObject dbDoc0 = new BasicDBObject();
    	mongoTemplate.getConverter().write(mailbox0, dbDoc0); //it is the one spring use for convertions.
    	Update update0 = Update.fromDBObject(dbDoc0);

    	mongoTemplate.upsert(Query.query(Criteria.where("user_id").is("1")), 
    			update0, MailBox.class);
    	
	   	MailBox mailbox = new MailBox();
	   	
    	Mails mail = new Mails();
    	mail.setConversationId("1");
    	mail.setUserId("2");
    	mail.setUnread(0);
    	mail.setFromFirstName("khim");
    	
    	mailbox.setUserId("2");
    	mailbox.getMails().add(mail);
    	
    	//build update
    	DBObject dbDoc = new BasicDBObject();
    	mongoTemplate.getConverter().write(mailbox, dbDoc); //it is the one spring use for convertions.
    	Update update = Update.fromDBObject(dbDoc);

    	mongoTemplate.upsert(Query.query(Criteria.where("user_id").is("2")), 
    			update, MailBox.class);
    	
    	boolean exist = mongoTemplate.exists(Query.query(Criteria.where("user_id").is("2")), MailBox.class);
    	
    	System.out.println("Mailbox exist " + exist);
 
    	Mails mail2 = new Mails();
    	mail2.setConversationId("3");
    	mail2.setUnread(0);
    	mail2.setUserId("2");
    	mail2.setFromFirstName("John");
    	
    	mongoTemplate.updateFirst(Query.query(Criteria.where("user_id").is("2")), 
    			new Update().push("mails", mail2), MailBox.class);
    	
    	Mails mail3 = new Mails();
    	mail3.setConversationId("4");
    	mail3.setUserId("2");
    	mail3.setUnread(0);
    	mail3.setDeleted(1);
    	mail3.setFromFirstName("Jay");

    	/*
    	 * use push if mails was an array and not a map like below
    	 */
    	mongoTemplate.updateFirst(Query.query(Criteria.where("user_id").is("2")), 
    			new Update().push("mails", mail3), MailBox.class);
    	
    	
    	mongoTemplate.findAndModify(Query.query(new Criteria().andOperator(Criteria.where("user_id").is("2"), Criteria.where("mails").elemMatch(Criteria.where("mails.conversation_id").is("3")))), 
    			new Update().inc("mails.$.unread", 1), Mails.class);
    	
		
		TypedAggregation<MailBox> agg = newAggregation(MailBox.class, //
				match(Criteria.where("userId").is("2")),
				unwind("mails"), //
				match(Criteria.where("mails.conversationId").is("3")),
				project().and("mails.conversationId").as("conversation_id")
				.and("mails.unread").as("unread")
				.and("mails.created").as("created")
				.and("mails.fromFirstName").as("from_first_name")
				.and("mails.fromUserId").as("from_user_id")
				.and("mails.lastMessage").as("last_message"));

		AggregationResults<Mails> results = mongoTemplate.aggregate(agg, Mails.class);
		Assert.assertEquals(1, results.getMappedResults().size());
		Mails resultMail = results.getMappedResults().get(0);

		
		System.out.println("Found Box " + results.getMappedResults().size());    	
	}

	@Test
	public void testUpdateSubArrayList() {
	MailBox mailbox0 = new MailBox();
	   	
    	Mails mail0 = new Mails();
    	mail0.setConversationId("3");
    	mail0.setUserId("1");
    	mail0.setLastMessage("testtest");
    	mail0.setUnread(0);
    	mail0.setFromFirstName("John");
    	
    	mailbox0.setUserId("1");
    	mailbox0.getMails().add(mail0);
    	
    	//build update
    	DBObject dbDoc0 = new BasicDBObject();
    	mongoTemplate.getConverter().write(mailbox0, dbDoc0); //it is the one spring use for convertions.
    	Update update0 = Update.fromDBObject(dbDoc0);

    	mongoTemplate.upsert(Query.query(Criteria.where("user_id").is("1")), 
    			update0, MailBox.class);
    	
	   	MailBox mailbox = new MailBox();
	   	
    	Mails mail = new Mails();
    	mail.setConversationId("1");
    	mail.setUserId("2");
    	mail.setUnread(0);
    	mail.setLastMessage("testtest2");
    	mail.setFromFirstName("khim");
    	
    	mailbox.setUserId("2");
    	mailbox.getMails().add(mail);
    	
    	//build update
    	DBObject dbDoc = new BasicDBObject();
    	mongoTemplate.getConverter().write(mailbox, dbDoc); //it is the one spring use for convertions.
    	Update update = Update.fromDBObject(dbDoc);

    	mongoTemplate.upsert(Query.query(Criteria.where("user_id").is("2")), 
    			update, MailBox.class);
    	
    	boolean exist = mongoTemplate.exists(Query.query(Criteria.where("user_id").is("2")), MailBox.class);
    	

    	Mails mail2 = new Mails();
    	mail2.setConversationId("3");
    	mail2.setUnread(0);
    	mail2.setUserId("2");
    	mail2.setLastMessage("testtest3");
    	mail2.setFromFirstName("John");
    	
    	mongoTemplate.updateFirst(Query.query(Criteria.where("user_id").is("2")), 
    			new Update().push("mails", mail2), MailBox.class);
    	
    	Mails mail3 = new Mails();
    	mail3.setConversationId("4");
    	mail3.setUserId("2");
    	mail3.setUnread(0);
    	mail3.setDeleted(1);
    	mail3.setLastMessage("testtest4");
    	mail3.setFromFirstName("Jay");

    	/*
    	 * use push if mails was an array and not a map like below
    	 */
    	mongoTemplate.updateFirst(Query.query(Criteria.where("user_id").is("2")), 
    			new Update().push("mails", mail3), MailBox.class);

		
		TypedAggregation<MailBox> agg = newAggregation(MailBox.class, //
				match(Criteria.where("userId").is("2")),
				unwind("mails"), //
				match(Criteria.where("mails.deleted").is(0)),
				project().and("mails.conversationId").as("conversation_id")
				.and("mails.unread").as("unread")
				.and("mails.created").as("created")
				.and("mails.fromFirstName").as("from_first_name")
				.and("mails.fromUserId").as("from_user_id")
				.and("mails.lastMessage").as("last_message"));

		AggregationResults<Mails> results = mongoTemplate.aggregate(agg, Mails.class);

		System.out.println("Found Box " + results.getMappedResults().size());

	}

	
	@Test
	public void testMailbox() {
	MailBox mailbox0 = new MailBox();
	   	
    	Mails mail0 = new Mails();
    	mail0.setConversationId("3");
    	mail0.setUserId("1");
    	mail0.setUnread(0);
    	mail0.setFromFirstName("John");
    	
    	mailbox0.setUserId("1");
    	mailbox0.getMails().add(mail0);
    	
    	//build update
    	DBObject dbDoc0 = new BasicDBObject();
    	mongoTemplate.getConverter().write(mailbox0, dbDoc0); //it is the one spring use for convertions.
    	Update update0 = Update.fromDBObject(dbDoc0);

    	mongoTemplate.upsert(Query.query(Criteria.where("user_id").is("1")), 
    			update0, MailBox.class);
    	
	   	MailBox mailbox = new MailBox();
	   	
    	Mails mail = new Mails();
    	mail.setConversationId("1");
    	mail.setUserId("2");
    	mail.setUnread(0);
    	mail.setFromFirstName("khim");
    	
    	mailbox.setUserId("2");
    	mailbox.getMails().add(mail);
    	
    	//build update
    	DBObject dbDoc = new BasicDBObject();
    	mongoTemplate.getConverter().write(mailbox, dbDoc); //it is the one spring use for convertions.
    	Update update = Update.fromDBObject(dbDoc);

    	mongoTemplate.upsert(Query.query(Criteria.where("user_id").is("2")), 
    			update, MailBox.class);
    	
    	boolean exist = mongoTemplate.exists(Query.query(Criteria.where("user_id").is("2")), MailBox.class);
    	
    	System.out.println("Mailbox exist " + exist);
 
    	Mails mail2 = new Mails();
    	mail2.setConversationId("3");
    	mail2.setUnread(0);
    	mail2.setUserId("2");
    	mail2.setFromFirstName("John");
    	
    	mongoTemplate.updateFirst(Query.query(Criteria.where("user_id").is("2")), 
    			new Update().push("mails", mail2), MailBox.class);
    	
    	Mails mail3 = new Mails();
    	mail3.setConversationId("4");
    	mail3.setUserId("2");
    	mail3.setUnread(0);
    	mail3.setDeleted(1);
    	mail3.setFromFirstName("Jay");

    	/*
    	 * use push if mails was an array and not a map like below
    	 */
    	mongoTemplate.updateFirst(Query.query(Criteria.where("user_id").is("2")), 
    			new Update().push("mails", mail3), MailBox.class);
    	

    	// db.mailbox.update({"user_id": "2", mails: {$elemMatch: {conversation_id: "3"}} , {$inc:{"mails.$.unread":1}})
    	// Great for updating a field in an array but not for incrementing a value in an array
    	//mongoTemplate.findAndModify(Query.query(Criteria.where("user_id").is("2").and("mails").elemMatch(Criteria.where("conversation_id").is("3"))), 
    	//		new Update().inc("mails.$.unread", 10), MailBox.class);
    	
    	mongoTemplate.findAndModify(Query.query(Criteria.where("user_id").is("2").and("mails").elemMatch(Criteria.where("conversation_id").is("3"))), 
    			new Update().inc("mails.$.unread", 5).set("mails.$.last_message", "hello"), MailBox.class);    	

    	mongoTemplate.findAndModify(Query.query(Criteria.where("user_id").is("2")), 
    			new Update().inc("mails.3.unread", 1), MailBox.class);
    	
    	List<MailBox> mailBoxes = mongoTemplate.find(Query.query(Criteria.where("user_id").is("2")), MailBox.class);
    
    	System.out.println("size " + mailBoxes.size());
    	
    	for(MailBox box : mailBoxes) {
    		System.out.println("mails " + box.getMails().size());
    	}

    	//{ "user_id" : "2" , "_id" : 0 , "mails" : { "$elemMatch" : { "conversation_id" : "3"}}}
    	
    	
    	// db.mailbox.find( { "user_id" : "2"}, {_id: 0, mails: {$elemMatch: {conversation_id: "3"}}});
    	
    	Query test = new Query(Criteria.where("user_id").is("2").where("mails").elemMatch(Criteria.where("conversation_id").is("3")));
    	
    	//Query find = new Query(Criteria.where("user_id").is("2")).addCriteria(Criteria.where("_id").is(0)).addCriteria(Criteria.where("mails").elemMatch(Criteria.where("conversation_id").is("3") ));
    	
    	Query find = new Query(Criteria.where("mails").elemMatch(Criteria.where("user_id").is("2").and("conversation_id").is("3").and("mails.$").is(1)));
    	
    	Query find2 = Query.query(Criteria.where("mails.$").is(1).and("_id").is(0).and("mails").elemMatch(Criteria.where("conversation_id").is("3").and("user_id").is("2") ));

    	BasicQuery query = new BasicQuery("{ \"user_id\" : \"2\"}, {_id: 0, mails: {$elemMatch: {conversation_id: \"3\"}}}");
    	//List<Mails> result = mongoTemplate.findOne(query, Mails.class);    
    	

    	Mails foundMail = mongoTemplate.findOne(find, Mails.class);
 
    	//Mails foundMail = mongoTemplate.getConverter().read(Mails.class, dbObject); 


    	if(foundMail != null) {
    		System.out.println("found mail " + foundMail.getConversationId());
    	}
    	/*
    	 * mongoTemplate.upsert(Query.query(Criteria.where("user_id").is("2").andOperator(Criteria.where("mails.conversation_id").is("3"))), 
    			new Update().push("mails", mail2), MailBox.class);
    			*/
    	
    	
    	/*
    	 * Query query = new Query(new Criteria().andOperator(
  Criteria.where("pollID").is("ABCDEFG"),
  Criteria.where("commentDetailList").elemMatch(Criteria.where("user.userID").is("001"))
));
    	 */
    	
    	List<Mails> foundMails = mongoTemplate.find(Query.query(Criteria.where("mails").elemMatch(Criteria.where("deleted").ne("1").and("mails.$").is(1))), Mails.class);
    	System.out.println("Found Box " + foundMails.size());
	}


	@Test
	public void testStoreUpdate() {
		Query query = new Query();
		query.addCriteria(Criteria.where("name").is("khim1"));

		List<Store> stores = mongoTemplate.find(query, Store.class, "bname");
		
		if(stores != null) {
			for(Store s : stores) {

			}
		}
		

		
		DBCollection collection = mongoTemplate.getCollection("bname");
		BulkWriteOperation bulk = collection.initializeOrderedBulkOperation();
		

		for(int i = 0; i < 3; i ++) {

			BasicDBObject searchObject = new BasicDBObject();
			searchObject.put("name", "khim" + i);

					
			DBObject modifiedObject =new BasicDBObject();
			modifiedObject.put("$set", new BasicDBObject()
				.append("name", "khim" + i)
				.append("address_line_1", "123 khim dr suite " + i)
				.append("address_line_2", "KHIM " + i)
				.append("city", "los angeles")
				.append("state", "ca")
			);
					
			bulk.find(searchObject).
			upsert().update(modifiedObject);
		}
		
		BulkWriteResult writeResult = bulk.execute();

		stores = mongoTemplate.find(query, Store.class, "bname");
		
		for(Store s : stores) {
			System.out.println("store city ");
		}
	}
	
	@Test
	public void testFindFromMongo() {
		String testString = "james j davis elementary school,29940,seabrook,sc,364 kean neck rd";
		Query query = new Query();
		query.addCriteria(Criteria.where("name").is(testString));
		
		List<String> keys = mongoTemplate.find(query, String.class, "store_keys");
		
		if(keys != null) {
			System.out.println("Found from mongo keys : " + keys.size());
			if(keys.size() > 0 && keys.get(0) != null) {
				System.out.println(" key " + keys.get(0));
			}
		}
		System.out.println("Key from mongo is null");
	}
	
	@Test
	public void testMongoUpsertStore() {
		DBCollection collection = mongoTemplate.getCollection("bname");
		BulkWriteOperation bulk = collection.initializeOrderedBulkOperation();
		
		BasicDBObject searchObject = new BasicDBObject();
		searchObject.put("store_id",  new Long(1));

		DBObject modifiedObject =new BasicDBObject();
		modifiedObject.put("$set", new BasicDBObject()
			.append("public_store_key", "test")
			.append("key_words", "test")
		);
				
		bulk.find(searchObject).
		upsert().update(modifiedObject);
	
		BulkWriteResult writeResult = bulk.execute();
	}
	
	/**
	 * creates array of {frequency_key: xxxxx, dates: {20160501: 3}, {20160503: 4}}
	 */
	@Test
	public void testMongoUpsert() {
		DBCollection collection = mongoTemplate.getCollection("spending_pattern_promo_by_lifetime");
		BulkWriteOperation bulk = collection.initializeOrderedBulkOperation();

		DBObject modifiedObject =new BasicDBObject();

	    BasicDBObject query = new BasicDBObject();
	    query.put("frequency_key", "1-deposit-UserSpendingByLifetime");

	    BasicDBObject incValue = new BasicDBObject("dates.20160508", 5f);	    

	    modifiedObject.put("$set",  new BasicDBObject().append("frequency_key", "1-deposit-UserSpendingByLifetime"));
	    modifiedObject.put("$inc", incValue);
	    
	    // single update works
	    //collection.update(query, modifiedObject, true, false, WriteConcern.SAFE);

	    // multi update
		bulk.find(query).
		upsert().update(modifiedObject);	
		
		BulkWriteResult writeResult = bulk.execute();
	}
	
	/*
	 * 
	 */
	@Test
	public void testMongoUpsert2() {
		DBCollection collection = mongoTemplate.getCollection("spending_pattern_promo_by_lifetime");
		BulkWriteOperation bulk = collection.initializeOrderedBulkOperation();

		DBObject modifiedObject =new BasicDBObject();

	    BasicDBObject query = new BasicDBObject();
	    query.put("frequency_key", "1-deposit-UserSpendingByLifetime");

	    BasicDBList list = new BasicDBList();
	    BasicDBObject incValue = new BasicDBObject("date", "20160508").append("value", 5f);
	    list.add(incValue);
	    //query.put("$push", list);

	    modifiedObject.put("$set",  new BasicDBObject().append("frequency_key", "1-deposit-UserSpendingByLifetime"));
	    modifiedObject.put("$push", list);
	    modifiedObject.put("$inc", new BasicDBObject("date", "20160508").append("value", 5f));
	    
	    // multi update
		bulk.find(query).
		upsert().update(modifiedObject);	
		
		BulkWriteResult writeResult = bulk.execute();
	}
	
	
	@Test
	public void testFlattenScore() {
		DBCollection collection = mongoTemplate.getCollection("spending_pattern_promo_by_lifetime");

		DBObject object = BasicDBObjectBuilder.start("$gte", "20160501").add("$lte", "20160509").get();
		
		BasicDBObject match = new BasicDBObject("$match", new BasicDBObject());
		match.append("dates.date", object);
		 
		
		BasicDBObject group = new BasicDBObject(
			    "$group", new BasicDBObject("_id", "$frequency_key")
			     //.append("date", BasicDBObjectBuilder.start("$gte", Long.parseLong(DateFormatUtil.moveDaysBy(-90))).add("$lte", Long.parseLong(DateFormatUtil.moveDaysBy(0))));				 
			    .append("total", new BasicDBObject( "$sum", "$date")
			    )
		);
		
		/*
		 *     MongoClient mongoClient = null;

    try {
        mongoClient = new MongoClient("localhost", 27017);
    } catch (UnknownHostException e) {}

    DB db = mongoClient.getDB("test");

    DBCollection collection = db.getCollection("user");

    DBObject match = new BasicDBObject("$match", new BasicDBObject("id", "11"));
    DBObject groupFields = new BasicDBObject("_id", "$name");
    groupFields.put("name", new BasicDBObject("$addToSet", "$name"));
    DBObject group = new BasicDBObject("$group", groupFields);

    AggregationOutput output = collection.aggregate(match, group);

    Iterable<DBObject> itResult = output.results();

    for (DBObject dbo : itResult) {
        List<String> items = (List<String>) dbo.get("name");
        for(String item : items){
            System.out.println(item);
        }

    }
		 */

		// run aggregation
        AggregationOutput output = null;
        try {
        	output = collection.aggregate(Arrays.asList(new DBObject[] {match, group}));
        } catch (MongoException e) {
        	e.printStackTrace();
        }

        Iterator<DBObject> it = output.results().iterator();

        while ( it.hasNext()) {
        	BasicDBObject basicDBObject = (BasicDBObject) it.next();
        	String key = basicDBObject.get("_id").toString();
       		String sum = basicDBObject.get("total").toString();
  
       		System.out.println(key + " : " + sum);
        }
	}
}
