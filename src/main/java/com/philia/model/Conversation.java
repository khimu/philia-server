package com.philia.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Holds the list of messages going back and forth between the 2 users
 * 
 * Conversations are stored as a document in its own mongo collection
 * 
 * The conversation is retrieved and stored by the participant's user ids
 * 
 * @author khimung
 *
 */
@XmlRootElement
@Document(collection = "conversation")
public class Conversation extends Base {

	private static final long serialVersionUID = 1L;

	/*
	 * Concatenate the userID of user 1 with user 2. The lower ID comes before
	 * the bigger ID. Use this to retrieve the conversation on drill down
	 */
	@Field(value="conversation_id")
	private String conversationId;
	
	private List<Message> messages;

	public List<Message> getMessages() {
		if(messages == null) {
			messages = new ArrayList<Message>();
		}
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

	public String getConversationId() {
		return conversationId;
	}

	public void setConversationId(String conversationId) {
		this.conversationId = conversationId;
	}

}
