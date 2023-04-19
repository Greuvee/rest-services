package com.acme.rest.controller;

import java.util.Arrays;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acme.integration.meeting.ZoomClient;
import com.acme.integration.meeting.data.ZoomMeeting;
import com.acme.jpa.entity.MeetingEntity;
import com.acme.jpa.entity.UserAvailabilityTimeEntity;
import com.acme.jpa.entity.UserEntity;
import com.acme.jpa.repo.MeetingRepository;
import com.acme.jpa.repo.UserAvailabilityTimeRepository;
import com.acme.jpa.repo.UserRepository;
import com.acme.rest.controller.model.MeetingModel;
import com.acme.rest.controller.model.request.MeetingRequest;
import com.acme.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/meetings")
public class MeetingController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MeetingController.class);

	@Autowired
	private UserAvailabilityTimeRepository userAvailabilityTimeRepository;
	
	@Autowired
	private MeetingRepository meetingRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ZoomClient zoomClient;
	
	@PostMapping("/schedule")
	public ResponseEntity<MeetingModel> scheduleMeeting(HttpServletRequest request, @RequestBody MeetingRequest meetingRequest) {
		
		try {
			Optional<UserEntity> advocateUserLookup = userRepository.findById(meetingRequest.getAdvocateUuid());
			Optional<UserEntity> clientUserLookup = userService.getAuthenticatedUser(request);
			
			if(advocateUserLookup.isEmpty()) {
				return ResponseEntity.badRequest().body(new MeetingModel(String.format("Could not schedule meeting for advocate %s (not found)", meetingRequest.getAdvocateUuid()), false));
			
			}else if(clientUserLookup.isEmpty()) {
				return ResponseEntity.badRequest().body(new MeetingModel("Could not schedule meeting for current user (not found)", false));
			}else {
				UserEntity advocateUser = advocateUserLookup.get();
				UserEntity clientUser = clientUserLookup.get();
				
				Optional<UserAvailabilityTimeEntity> advocateTimeSlot =  userAvailabilityTimeRepository.findById(meetingRequest.getTimeSlotUuid());
				if(advocateTimeSlot.isPresent()) {
					UserAvailabilityTimeEntity ts = advocateTimeSlot.get();
					if(ts.isAvailable()) {
						
						ZoomMeeting meeting  = zoomClient.createMeeting(clientUser, advocateUser.getEmailAddress());
						
						MeetingEntity meetingEntity = meetingRepository.save(new MeetingEntity(Arrays.asList(clientUser, advocateUser), meeting));
						
						advocateUser.addMeeting(meetingEntity);
						clientUser.addMeeting(meetingEntity);
						
						userRepository.saveAll(Arrays.asList(advocateUser, clientUser));
						
						return ResponseEntity.ok(new MeetingModel(meetingEntity, "Meeting successfully created", true));
					}else {
						return ResponseEntity.badRequest().body(new MeetingModel(String.format("Time slot %s is not available for advocate user %s", ts.getUuid(), advocateUser.getUuid()), false));
					}
				}else {
					return ResponseEntity.badRequest().body(new MeetingModel(String.format("Could not locate time slot %s for advocate %s", meetingRequest.getTimeSlotUuid(), advocateUser.getUuid()), false));
				}
			}
		}catch(Exception e) {
			LOGGER.error(e.getMessage(), e);
			return ResponseEntity.internalServerError().body(new MeetingModel(String.format("Schedule meeting error: %s", e.getMessage()), false));
		}	
	}
}