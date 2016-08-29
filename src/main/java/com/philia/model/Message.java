package com.philia.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A message between 2 users
 * 
 * @author khimung
 *
 */
@XmlRootElement
public class Message extends Base {

	private static final long serialVersionUID = 1L;
	
	
	private String message;
	
	/*
	 * from user id
	 */
	private String from;

	/*
	 * to user id
	 */
	private String to;
	
	
	private String toFirstName;
	
	
	private String fromFirstName;
	
	/*
	 * forgot what read is for;
	 * 
	 * 0 = already read
	 * 1 = have not read
	 */
	private Integer read = 0;
	
	/*
	 * forgot what deleted is for
	 * 
	 * 0 = active
	 * 1 = deleted
	 */
	private Integer deleted = 0;
	
	
	private Date created;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public Integer getRead() {
		return read;
	}

	public void setRead(Integer read) {
		this.read = read;
	}

	public Integer getDeleted() {
		return deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

	public String getToFirstName() {
		return toFirstName;
	}

	public void setToFirstName(String toFirstName) {
		this.toFirstName = toFirstName;
	}

	public String getFromFirstName() {
		return fromFirstName;
	}

	public void setFromFirstName(String fromFirstName) {
		this.fromFirstName = fromFirstName;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

}
