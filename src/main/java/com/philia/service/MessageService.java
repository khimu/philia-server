package com.philia.service;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.philia.model.Conversation;
import com.philia.model.MailBox;
import com.philia.model.Mails;
import com.philia.model.Message;

/**
 * 
 * @author khimung
 *
 */
@Repository("messageService")
public class MessageService implements IMessageService {
	private final static Logger logger = Logger.getLogger(MessageService.class);

	@Resource
	private MongoOperations mongoTemplate;
	
	/**
	 * Add a new message
	 * 
	 * @param message
	 */
	public void createMessage(Message message) {
		
		String fromUserId = message.getFrom();
		String toUserId = message.getTo();
		String conversationId = createConversationId(toUserId, fromUserId);
		
		/*
		 * save message to mongo
		 */
		mongoTemplate.save(message);
		
		/*
		 * convenient way to allow for push alerts by just retireving only the mailbox with the conversations and the unread counts
		 */
		boolean toUserMailBoxExist = mongoTemplate.exists(Query.query(Criteria.where("user_id").is(toUserId)), MailBox.class);
		boolean fromUserMailBoxExist = mongoTemplate.exists(Query.query(Criteria.where("user_id").is(fromUserId)), MailBox.class);
		
		// Find the Conversation and add the message to the existing conversation
	   	boolean conversationExist = mongoTemplate.exists(Query.query(Criteria.where("conversation_id").is(conversationId)), Conversation.class);
    	
	   	if(conversationExist == false) {
	    	Conversation conversation = new Conversation();
	    	conversation.setConversationId(conversationId);
	    	conversation.getMessages().add(message);
	    	createConversation(conversation, conversationId);
	    	
		   	// Create a mail record to hold the conversation that toUser is having with fromUser
		   	Mails toUserMails = new Mails();
		   	toUserMails.setUserId(toUserId);
		   	toUserMails.setConversationId(conversationId);
		   	/*
		   	 * from user's first name
		   	 */
		   	toUserMails.setFromFirstName(message.getFromFirstName());
		   	toUserMails.setFromUserId(fromUserId);
		   	toUserMails.setCreated(new Date());
		   	toUserMails.setLastMessage(message.getMessage());
		   	toUserMails.setUnread(1);
		   	
		   	
		   	
		   	// Create a mail record to hold the conversation that fromUser is having with toUser
		   	Mails fromUserMails = new Mails();
		   	fromUserMails.setUserId(fromUserId);
		   	fromUserMails.setConversationId(conversationId);
		   	/*
		   	 * from user's first name
		   	 */
		   	fromUserMails.setFromFirstName(message.getToFirstName());
		   	fromUserMails.setFromUserId(toUserId);
		   	fromUserMails.setCreated(new Date());
		   	fromUserMails.setLastMessage(message.getMessage());
		   	fromUserMails.setUnread(0);
		   	
	    	if(toUserMailBoxExist == false) {
	    		MailBox box = new MailBox();
	    		box.setUserId(toUserId);
	    		box.getMails().add(toUserMails);
	    		createMailbox(box, toUserId);
	    	}
	    	else {
	    		addMails(toUserMails, toUserId);
	    	}
	    	
	    	if(fromUserMailBoxExist == false) {
	    		MailBox box = new MailBox();
	    		box.setUserId(fromUserId);
	    		box.getMails().add(fromUserMails);
	    		createMailbox(box, fromUserId);
	    	}
	    	else {
	    		addMails(fromUserMails, fromUserId);
	    	}
	    }
	   	else {
	   		// add the new message
	   		addMessage(message, conversationId);
	   		
	   		/**
	   		 * This needs to be refactored to update the unread for the 1 mails record vs read from mongo and update to mongo
	   		 */
			TypedAggregation<MailBox> agg = newAggregation(MailBox.class, //
					match(Criteria.where("userId").is(toUserId)),
					unwind("mails"), //
					match(Criteria.where("mails.conversationId").is(conversationId)),
					project().and("mails.conversationId").as("conversation_id")
					.and("mails.unread").as("unread")
					.and("mails.created").as("created")
					.and("mails.fromFirstName").as("from_first_name")
					.and("mails.fromUserId").as("from_user_id")
					.and("mails.lastMessage").as("last_message"));

			AggregationResults<Mails> results = mongoTemplate.aggregate(agg, Mails.class);
			
	   		// find the mail for the conversation from the mailbox of to user and update the unread count
	   		Mails toUserMail = results.getMappedResults().stream().filter(c -> c.getConversationId().equals(conversationId)).findFirst().get();
	   		updateMailUnread(toUserId, conversationId, toUserMail.getUnread() + 1, message.getMessage());
	   	}
	}
	
	/*
	 * Always order the user id in ascending order so it doesn't matther
	 * who wrote the message, the conversation key is always the same
	 * so both user can pull the same conversation.  Reduces storage
	 */
	private String createConversationId(String to, String from) {
		int toId = Integer.parseInt(to);
		int fromId = Integer.parseInt(from);
		
		if(toId < fromId) {
			return to+from;
		}
		
		return from+to;
	}
	
	private void addMessage(Message message, String conversationId) {
    	mongoTemplate.updateFirst(Query.query(Criteria.where("conversation_id").is(conversationId)), 
    			new Update().push("messages", message), Conversation.class);
	}
	
	private void createConversation(Conversation conversation, String conversationId) {
    	//build update
    	DBObject dbDoc = new BasicDBObject();
    	mongoTemplate.getConverter().write(conversation, dbDoc); //it is the one spring use for convertions.
    	Update update = Update.fromDBObject(dbDoc);
 
    	mongoTemplate.upsert(Query.query(Criteria.where("conversation_id").is(conversationId)), 
    			update, Conversation.class);
	}
	
	private void updateMailUnread(String userId, String conversationId, int count, String message) {
    	mongoTemplate.findAndModify(Query.query(Criteria.where("user_id").is(userId).and("mails").elemMatch(Criteria.where("conversation_id").is(conversationId))), 
    			new Update().inc("mails.$.unread", count).set("mails.$.last_message", message), MailBox.class);
	}
	
	private void createMailbox(MailBox mailbox, String userId) {
    	//build update
    	DBObject dbDoc = new BasicDBObject();
    	mongoTemplate.getConverter().write(mailbox, dbDoc); //it is the one spring use for convertions.
    	Update update = Update.fromDBObject(dbDoc);

 
    	mongoTemplate.upsert(Query.query(Criteria.where("user_id").is(userId)), 
    			update, MailBox.class);
	}
	
	private void addMails(Mails mail, String userId) {
    	mongoTemplate.updateFirst(Query.query(Criteria.where("user_id").is(userId)), 
    			new Update().push("mails", mail), MailBox.class);
	}
	
	public MailBox getMailBox(String userId) {
    	MailBox foundBox = mongoTemplate.findOne(Query.query(Criteria.where("user_id").is(userId)), MailBox.class);
    	MailBox result = new MailBox();
    	result.setUserId(userId);
    	List<Mails> mails = foundBox.getMails().stream().filter(m -> m.getDeleted() == 0).collect(Collectors.toList());
    	result.setMails(mails);
    	return result;
	}
	
	public Conversation getConversation(String conversationId) {
		return mongoTemplate.findOne(Query.query(Criteria.where("conversation_id").is(conversationId)), Conversation.class);
	}
}
