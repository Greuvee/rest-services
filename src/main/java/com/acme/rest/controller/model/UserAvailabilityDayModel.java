package com.acme.rest.controller.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;

import com.acme.jpa.entity.UserAvailabilityDayEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true, value = { "sortOrder" })
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserAvailabilityDayModel {
	
	private String uuid;
	
	private Long sortOrder;
	
	private String day;
	
	private List<UserAvailabilityTimeModel> timeSlots;
	
	private boolean available;

	public UserAvailabilityDayModel() {
		super();
	}
	
	public UserAvailabilityDayModel(UserAvailabilityDayEntity entity) {
		this.uuid = entity.getUuid();
		this.sortOrder = entity.getSortOrder();
		this.day = entity.getDay();
		
		if(CollectionUtils.isNotEmpty(entity.getTimeSlots())) {
			entity.getTimeSlots()
				.stream()
				.filter(Objects::nonNull)
				.forEach(e -> addTimeSlot(new UserAvailabilityTimeModel(e)));
		}
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
		this.day = day;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public List<UserAvailabilityTimeModel> getTimeSlots() {
		if(this.timeSlots == null || this.timeSlots.isEmpty()) {
			return Collections.emptyList();
		}else {
			return this.timeSlots
				.stream()
				.filter(Objects::nonNull)
				.sorted( (UserAvailabilityTimeModel o1, UserAvailabilityTimeModel o2) -> 
					o1 != null && o2 != null ? Long.compare(o1.getSortOrder(), o2.getSortOrder()) : 0
				)
				.toList();
		}
	}
	
	private void addTimeSlot(UserAvailabilityTimeModel timeSlot) {
		if(this.timeSlots == null) {
			this.timeSlots = new ArrayList<>();
		}
		this.timeSlots.add(timeSlot);
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
}