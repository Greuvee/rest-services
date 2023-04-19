package com.acme.rest.controller.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MeetingRequest {

	private String userUuid;
	
	private String advocateUuid;
	
	private String dayAvailableUuid;
	
	private String timeSlotUuid;

	public String getAdvocateUuid() {
		return advocateUuid;
	}

	public void setAdvocateUuid(String advocateUuid) {
		this.advocateUuid = advocateUuid;
	}

	public String getDayAvailableUuid() {
		return dayAvailableUuid;
	}

	public void setDayAvailableUuid(String dayAvailableUuid) {
		this.dayAvailableUuid = dayAvailableUuid;
	}

	public String getTimeSlotUuid() {
		return timeSlotUuid;
	}

	public void setTimeSlotUuid(String timeSlotUuid) {
		this.timeSlotUuid = timeSlotUuid;
	}

	public String getUserUuid() {
		return userUuid;
	}

	public void setUserUuid(String userUuid) {
		this.userUuid = userUuid;
	}
	
	
}
