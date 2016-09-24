package com.philia.model;

import java.math.BigInteger;
import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * When a new user is created, for every user the new user matches to, a new
 * match record is created for the new user and added to the matching user's
 * list
 * 
 * @author khimung
 *
 */
@XmlRootElement
@Document(collection = "match")
public class Match extends Base {

	private static final long serialVersionUID = 1L;
	
	@Field(value="user_id")
	private String userId;

	/*
	 * The user id that matches to the user that this match record belongs to
	 */
	@Field(value="matched_with_user_id")
	private String matchedWithUserId;

	/*
	 * how far has the matcher gone with the matchee
	 */
	private Integer stage = 0;

	/*
	 * how well did this user match with the match record owner on a scale of 0
	 * to 100 and 100 being matched on all aspect
	 */
	private Integer weight = 100;

	private Date updated;
	
	private Date created;
	
	/*
	 * Path to physical image
	 */
	@Field(value="clear_image")
	private String clearImage;
	
	@Field(value="blurred_image")
	private String blurredImage;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getMatchedWithUserId() {
		return matchedWithUserId;
	}

	public void setMatchedWithUserId(String matchedWithUserId) {
		this.matchedWithUserId = matchedWithUserId;
	}

	public Integer getStage() {
		return stage;
	}

	public void setStage(Integer stage) {
		this.stage = stage;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public String getClearImage() {
		return clearImage;
	}

	public void setClearImage(String clearImage) {
		this.clearImage = clearImage;
	}

	public String getBlurredImage() {
		return blurredImage;
	}

	public void setBlurredImage(String blurredImage) {
		this.blurredImage = blurredImage;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

}
