package com.philia.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Create a mailbox mongo collection where the mails are stored and retrieved by
 * the user's ID in sorted order and with limit
 * 
 * @author khimung
 *
 */
@XmlRootElement
@Document(collection = "mailbox")
public class MailBox extends Base {

	private static final long serialVersionUID = 1L;

	/*
	 * the user id the mail box belongs to
	 */
	//@Id
	@Field(value="user_id")
	private String userId;

	/*
	 * list of user's conversations  
	 */
	private List<Mails> mails;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public List<Mails> getMails() {
		if(mails == null) {
			mails = new ArrayList<Mails>();
		}
		return mails;
	}

	public void setMails(List<Mails> mails) {
		this.mails = mails;
	}

}
