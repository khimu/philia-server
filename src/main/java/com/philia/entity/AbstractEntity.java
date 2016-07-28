package com.philia.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.AbstractPersistable;

/*
 * ID is declared in AbstractPersistable
 * 
 */
@MappedSuperclass
public abstract class AbstractEntity extends AbstractPersistable<BigInteger> implements Serializable{

	@Column(name = "created")
	@CreatedDate
	protected LocalDateTime createdDate;
	
	@Column(name = "updated")
	@LastModifiedDate
	protected LocalDateTime modifiedDate;

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public LocalDateTime getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(LocalDateTime modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

}
