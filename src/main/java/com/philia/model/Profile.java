package com.philia.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Profile is stored to mongo in order to be searched and matched
 * 
 * @author khimung
 *
 */
@XmlRootElement
@Document(collection = "profile")
public class Profile extends Base {

	private static final long serialVersionUID = 1L;

	@Field(value="user_id")
	private String userId;

	/*
	 * family, travel, education, etc
	 */
	private Integer interest;

	/*
	 * like men, woman, or both
	 */
	@Field(value="gender_interest")
	private Integer genderInterest = 0;

	/*
	 * marriage, hanging out, or curious
	 */
	@Field(value="dating_intension")
	private Integer datingIntension = 0;

	private String city;
	
	private String state;

	private String zipcode;
	
	private String country;

	private String firstName;
	
	private String lastName;

	private String birthdate;

	private String age;

	private String blurredImage;

	private String clearImage;

	private String email;
	

	/*
	 * updated must go back into the message queue to be rematched
	 */
	private Date updated;

	private Date created;
	

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getInterest() {
		return interest;
	}

	public void setInterest(Integer interest) {
		this.interest = interest;
	}

	public Integer getGenderInterest() {
		return genderInterest;
	}

	public void setGenderInterest(Integer genderInterest) {
		this.genderInterest = genderInterest;
	}

	public Integer getDatingIntension() {
		return datingIntension;
	}

	public void setDatingIntension(Integer datingIntension) {
		this.datingIntension = datingIntension;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getBlurredImage() {
		return blurredImage;
	}

	public void setBlurredImage(String blurredImage) {
		this.blurredImage = blurredImage;
	}

	public String getClearImage() {
		return clearImage;
	}

	public void setClearImage(String clearImage) {
		this.clearImage = clearImage;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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
