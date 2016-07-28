package com.philia.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * For each conversation, always create one record for each of the user's in the
 * conversation
 * 
 * @author khimung
 *
 */
@XmlRootElement
@Document(collection = "mails")
public class Mails extends Base {

	private static final long serialVersionUID = 1L;

	/*
	 * the user id the mail box belongs to
	 */
	// @Id
	@Field(value = "user_id")
	private String userId;

	/*
	 * The conversation is with the user
	 */
	@Field(value = "from_first_name")
	private String fromFirstName;

	@Field(value = "from_user_id")
	private String fromUserId;

	/*
	 * Concatenate the userID of user 1 with user 2. The lower ID comes before
	 * the bigger ID. Use this to retrieve the conversation on drill down
	 */
	@Field(value = "conversation_id")
	private String conversationId;

	@Field(value = "last_message")
	private String lastMessage;

	private Integer deleted = 0;

	/*
	 * Number of unread messages in the conversation thread Everytime the
	 * recipient sends a message, this count is incremented
	 */
	private Integer unread = 0;

	private Date created;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFromFirstName() {
		return fromFirstName;
	}

	public void setFromFirstName(String fromFirstName) {
		this.fromFirstName = fromFirstName;
	}

	public String getFromUserId() {
		return fromUserId;
	}

	public void setFromUserId(String fromUserId) {
		this.fromUserId = fromUserId;
	}

	public Integer getDeleted() {
		return deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

	public Integer getUnread() {
		return unread;
	}

	public void setUnread(Integer unread) {
		this.unread = unread;
	}

	public String getConversationId() {
		return conversationId;
	}

	public void setConversationId(String conversationId) {
		this.conversationId = conversationId;
	}

	public String getLastMessage() {
		return lastMessage;
	}

	public void setLastMessage(String lastMessage) {
		this.lastMessage = lastMessage;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

}
