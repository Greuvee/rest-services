package com.acme.jpa.entity;

import java.io.Serializable;
import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_validation")
public class UserValidationEntity implements Serializable {

	private static final long serialVersionUID = -1501316277219262944L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column
	private long id;
	
	@Column
	private Date dateSent;
	
	@Column
	private Date dateValidated;
	
	@Column
	private boolean validated;
	
	@Column
	private String type;
	
	@Column
	private int attemptCount;
	
	@Column
	private String token;
	
	@OneToOne
	private UserEntity user;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getDateSent() {
		return dateSent;
	}

	public void setDateSent(Date dateSent) {
		this.dateSent = dateSent;
	}

	public Date getDateValidated() {
		return dateValidated;
	}

	public void setDateValidated(Date dateValidated) {
		this.dateValidated = dateValidated;
	}
	
	public boolean isValidated() {
		return validated;
	}

	public void setValidated(boolean validated) {
		this.validated = validated;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getAttemptCount() {
		return attemptCount;
	}

	public void setAttemptCount(int attemptCount) {
		this.attemptCount = attemptCount;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		if(token != null) {
			this.token = token.trim();
		}
	}
	
}
