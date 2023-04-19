package com.acme.jpa.entity;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_availability_day")
public class UserAvailabilityDayEntity implements Serializable {
	
	private static final long serialVersionUID = 4024142921594143085L;

	@Id @GeneratedValue(strategy = GenerationType.UUID)
	private String uuid;
	
	@ManyToOne(optional=false) 
	@JoinColumn(name="user_id", nullable=false, updatable=false)
	private UserEntity user;
	
	@Column
	private long sortOrder;
	
	@Column
	private String day;
	
	@Column
	private Date dateCreated;
	
	@Column
	private Date dateUpdated;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="day", fetch = FetchType.EAGER)
	private List<UserAvailabilityTimeEntity> timeSlots;

	public UserAvailabilityDayEntity() {
		super();
	}
	
	public UserAvailabilityDayEntity(UserEntity user, String day, long sortOrder) {
		this();
		setUser(user);
		setDay(day);
		setSortOrder(sortOrder);
		setDateCreated(new Date(System.currentTimeMillis()));
	}
	
	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public Long getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Long sortOrder) {
		this.sortOrder = sortOrder;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		if(day != null) {
			this.day = day.trim();
		}
	}
	
	public String getUuid() {
		return uuid;
	}

	public void setId(String uuid) {
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

	public List<UserAvailabilityTimeEntity> getTimeSlots() {
		
		if(this.timeSlots == null) {
			this.timeSlots = new ArrayList<>();
		}
		
		return this.timeSlots
			.stream()
			.filter(Objects::nonNull)
			.sorted( (UserAvailabilityTimeEntity o1, UserAvailabilityTimeEntity o2) -> 
				(o1 != null && o2 != null) ? Long.compare(o1.getSortOrder(), o2.getSortOrder()) : 0
			)
			.toList();
	}

	public void setTimeSlots(List<UserAvailabilityTimeEntity> timeSlots) {
		if(CollectionUtils.isNotEmpty(timeSlots)) {
			this.timeSlots = timeSlots;
		}
	}
}