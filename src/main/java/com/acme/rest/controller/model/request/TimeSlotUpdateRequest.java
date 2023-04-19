package com.acme.rest.controller.model.request;

public class TimeSlotUpdateRequest {

	private String timeSlotUuid;
	
	private boolean available;

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public String getTimeSlotUuid() {
		return timeSlotUuid;
	}

	public void setTimeSlotUuid(String timeSlotUuid) {
		this.timeSlotUuid = timeSlotUuid;
	}
	
	
}
