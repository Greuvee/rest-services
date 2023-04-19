package com.acme.rest.controller.model;

import java.sql.Date;

import com.acme.jpa.entity.MeetingEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MeetingModel {

	private String uuid;
	
	private String startUrl;
	
	private String joinUrl;
	
	private String topic;
	
	private String agenda;
	
	private String startTime;
	
	private Integer duration;
	
	private String password;
	
	private Date createdDate;
	
	private String message;
	
	private Boolean created;
	
	public MeetingModel() {
		super();
	}
	
	public MeetingModel(String message, boolean created) {
		this();
		
		this.message = message;
		this.created = created;
	}
	
	public MeetingModel(MeetingEntity meeting, String message, boolean created) {
		this(message, created);
		
		if(meeting != null) {
			setUuid(meeting.getUuid());
			setStartUrl(meeting.getStartUrl());
			setJoinUrl(meeting.getJoinUrl());
			setTopic(meeting.getTopic());
			setAgenda(meeting.getAgenda());
			setStartTime(meeting.getStartTime());
			setDuration(meeting.getDuration());
			setPassword(meeting.getPassword());
			setCreatedDate(meeting.getCreatedDate());
		}
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getStartUrl() {
		return startUrl;
	}

	public void setStartUrl(String startUrl) {
		this.startUrl = startUrl;
	}

	public String getJoinUrl() {
		return joinUrl;
	}

	public void setJoinUrl(String joinUrl) {
		this.joinUrl = joinUrl;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getAgenda() {
		return agenda;
	}

	public void setAgenda(String agenda) {
		this.agenda = agenda;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Boolean isCreated() {
		return created;
	}

	public void setCreated(Boolean created) {
		this.created = created;
	}

	
	
}
