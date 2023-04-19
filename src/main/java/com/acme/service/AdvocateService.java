package com.acme.service;

import com.acme.jpa.entity.UserEntity;
import com.acme.rest.controller.model.UserAvailabilityTimeModel;
import com.acme.rest.controller.model.UserModel;
import com.acme.rest.controller.model.request.TimeSlotUpdateRequest;

import jakarta.servlet.http.HttpServletRequest;

public interface AdvocateService {
	
	UserModel initializeDefaultAvailability(String advocateUuid);
	
	UserModel getLoggedInAdvocate(HttpServletRequest request);

	UserEntity update(UserEntity advocate);
	
	UserAvailabilityTimeModel updateTimeSlot(HttpServletRequest request, TimeSlotUpdateRequest timeSlotUpdate);

}
