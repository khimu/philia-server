package com.philia.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="user")
@NamedQuery(name = "User.findByTheEmail", query = "from User u where u.email = ?")
public class User extends AbstractEntity  {

	private static final long serialVersionUID = 1L;

	@Column(unique = true)
	private String email;
	
	private String password;

	private String phone;
	
	@Column(name = "first_name")
	private String firstName;
	
	@Column(name = "last_name")
	private String lastName;

	private String city;
	private String state;
	private String zipcode;
	private String country;

	private Integer age;

	/*
	 * Path to physical image
	 */
	@Column(name = "clear_image")
	private String clearImage;
	
	@Column(name = "blurred_image")
	private String blurredImage;
	
	public User() {}

	public User(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
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

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

}
