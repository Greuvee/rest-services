package com.acme.rest.controller.model;

import java.util.Date;

import com.acme.jpa.entity.UserAvailabilityTimeEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true, value = { "sortOrder" })
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserAvailabilityTimeModel {
	
	private String uuid;
	
	private Long sortOrder;
	
	private String time;
	
	private boolean available;
	
	private Date dateUpdated;
	
	public UserAvailabilityTimeModel() {
		super();
	}
	
	public UserAvailabilityTimeModel(UserAvailabilityTimeEntity entity) {
		this.uuid = entity.getUuid();
		this.available = entity.isAvailable();
		this.sortOrder = entity.getSortOrder();
		this.time = entity.getTime();
		this.dateUpdated = entity.getDateUpdated();
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public Long getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Long sortOrder) {
		this.sortOrder = sortOrder;
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
	
	public Date getDateUpdated() {
		return dateUpdated;
	}

	public void setDateUpdated(Date dateUpdated) {
		this.dateUpdated = dateUpdated;
	}
	
}
