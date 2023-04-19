package com.acme.jpa.entity;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.acme.integration.meeting.data.ZoomMeeting;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "meetings")
public class MeetingEntity implements Serializable{

	private static final long serialVersionUID = -6714376084084830840L;

	@Id @GeneratedValue(strategy = GenerationType.UUID)
    private String uuid; 
	
	@Column
	private String zoomUuid;
	
	@Column(unique = true, length = 512)
	private String startUrl;
	
	@Column(unique = true)
	private String joinUrl;
	
	@Column
	private String topic;
	
	@Column
	private String agenda;
	
	@Column
	private String startTime;
	
	@Column
	private int duration;
	
	@Column
	private String password;
	
	@Column
	private boolean autoRecord;
	
	@Column
	private Date createdDate;
	
	@Column
	private Date modifiedDate;
	
	@ManyToMany(mappedBy="meetings")
	private List<UserEntity> users;
	
	public MeetingEntity() {
		setCreatedDate(new Date(System.currentTimeMillis()));
	}
	
	public MeetingEntity(List<UserEntity> users) {
		this();
		setUsers(users);
	}
	
	public MeetingEntity(List<UserEntity> users, ZoomMeeting newMeeting) {
		this(users);
		if(newMeeting != null) {
			setZoomUuid(newMeeting.getUuid());
			setPassword(newMeeting.getPassword());
			setAgenda(newMeeting.getAgenda());
			setTopic(newMeeting.getTopic());
			setStartTime(newMeeting.getStartTime());
			setDuration(newMeeting.getDuration());
			setStartUrl(newMeeting.getStartUrl());
			setJoinUrl(newMeeting.getJoinUrl());
		}
	}
	
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public List<UserEntity> getUsers() {
		if(this.users == null) {
			this.users = new ArrayList<>();
		}
		return users;
	}

	public void setUsers(List<UserEntity> users) {
		this.users = users;
	}
	
	public String getZoomUuid() {
		return zoomUuid;
	}

	public void setZoomUuid(String zoomUuid) {
		this.zoomUuid = zoomUuid;
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

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isAutoRecord() {
		return autoRecord;
	}

	public void setAutoRecord(boolean autoRecord) {
		this.autoRecord = autoRecord;
	}

	@Override
	public String toString() {
		return String.format("ZoomEventEntity["
				+ "uuid=%s, "
				+ "createdDate=%s, "
				+ "modifiedDate=%s, "
				+ "users=%s"
				+ "zoomUuid=%s, "
				+ "startUrl=%s, "
				+ "joinUrl=%s, "
				+ "topic=%s, "
				+ "agenda=%s, "
				+ "startTime=%s, "
				+ "duration=%s, "
				+ "password=%s, "
				+ "autoRecord=%s" 
				+ "]", 
				getUuid(),
				getCreatedDate(),
				getModifiedDate(),
				getUsers().stream().map(Object::toString).collect(Collectors.joining(" -- ")),
				getZoomUuid(),
				getStartUrl(),
				getJoinUrl(),
				getTopic(),
				getAgenda(),
				getStartTime(),
				getDuration(),
				getPassword(),
				isAutoRecord());	
	}
}
