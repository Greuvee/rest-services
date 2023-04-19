package com.acme.rest.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acme.rest.controller.model.UserModel;
import com.acme.rest.controller.model.request.TimeSlotUpdateRequest;
import com.acme.service.AdvocateService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/advocates")
public class AdvocateController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AdvocateController.class);
	@Autowired
	private AdvocateService advocateService;
	
	@GetMapping
	public ResponseEntity<UserModel> read(HttpServletRequest request) {
		try {
			return ResponseEntity.ok(advocateService.getLoggedInAdvocate(request));
		}catch(Exception e) {
			LOGGER.error("Exception caught loading current advocate", e);
			return ResponseEntity.internalServerError().body(new UserModel(e.getMessage()));
		}
	}
	
	@PostMapping("/availability/initialize")
	public ResponseEntity<UserModel> updateScheduleBlockOfTimeAvailability(HttpServletRequest request) {
		
		try {
			UserModel advocate = advocateService.getLoggedInAdvocate(request);
			return ResponseEntity.ok(advocateService.initializeDefaultAvailability(advocate.getUuid()));
		}catch(Exception e) {
			LOGGER.error("Exception during initialization of advocate availability", e);
			return ResponseEntity.internalServerError().body(new UserModel(e.getMessage()));
		}
	}
	
	@PostMapping("/availability/update")
	public ResponseEntity<UserModel> updateTimeSlot(HttpServletRequest request, @RequestBody TimeSlotUpdateRequest timeSlotUpdate) {
		try {
			advocateService.updateTimeSlot(request, timeSlotUpdate);
			return read(request);
		}catch(Exception e) {
			LOGGER.error("Exception updating advocate availability", e);
			return ResponseEntity.internalServerError().body(new UserModel(e.getMessage()));
		}
	}
	
}