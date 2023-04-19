package com.acme.jpa.entity;

import java.io.Serializable;
import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_availability_time")
public class UserAvailabilityTimeEntity implements Serializable {
	
	private static final long serialVersionUID = -24273241415044826L;

	@Id @GeneratedValue(strategy = GenerationType.UUID)
	private String uuid;
	
	@ManyToOne(optional=false) 
	@JoinColumn(name="day_id", nullable=false, updatable=false)
	private UserAvailabilityDayEntity day;
	
	@Column
	private long sortOrder;
	
	@Column
	private String time;
	
	@Column
	private boolean available;
	
	@Column
	private Date dateCreated;
	
	@Column
	private Date dateUpdated;
	
	public UserAvailabilityTimeEntity() {
		super();
	}
	
	public UserAvailabilityTimeEntity(UserAvailabilityDayEntity day, String time, long sortOrder) {
		this();
		setDay(day);
		setTime(time);
		setSortOrder(sortOrder);
		setDateCreated(new Date(System.currentTimeMillis()));
	}

	public UserAvailabilityDayEntity getDay() {
		return day;
	}

	public void setDay(UserAvailabilityDayEntity day) {
		this.day = day;
	}

	public Long getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Long sortOrder) {
		this.sortOrder = sortOrder;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		if(time != null) {
			this.time = time.trim();
		}
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getDateUpdated() {
		return dateUpdated;
	}

	public void setDateUpdated(Date dateUpdated) {
		this.dateUpdated = dateUpdated;
	}

	public void setSortOrder(long sortOrder) {
		this.sortOrder = sortOrder;
	}
	
}
