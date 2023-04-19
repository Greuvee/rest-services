package com.acme.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.acme.component.MessageSourceComponent;
import com.acme.jpa.entity.UserAvailabilityDayEntity;
import com.acme.jpa.entity.UserAvailabilityTimeEntity;
import com.acme.jpa.entity.UserEntity;
import com.acme.jpa.repo.UserAvailabilityDayRepository;
import com.acme.jpa.repo.UserAvailabilityTimeRepository;
import com.acme.jpa.repo.UserRepository;
import com.acme.rest.controller.model.UserAvailabilityTimeModel;
import com.acme.rest.controller.model.UserModel;
import com.acme.rest.controller.model.request.TimeSlotUpdateRequest;
import com.acme.security.JwtTokenService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

@Service
public class AdvocateServiceImpl implements AdvocateService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AdvocateServiceImpl.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private JwtTokenService jwtTokenService;
	
	@Autowired
	private UserAvailabilityDayRepository userAvailabilityDayRepository;
	
	@Autowired
	private UserAvailabilityTimeRepository userAvailabilityTimeRepository;
	
	@Autowired
	private MessageSourceComponent messageSourceComponent;
	
	@Override
	public UserEntity update(UserEntity advocate) {
		
		if(advocate != null) {
			advocate.setDateModified(new Date(System.currentTimeMillis()));
			return userRepository.save(advocate);
		}else {
			return null;
		}
	}
	
	@Override
	public UserAvailabilityTimeModel updateTimeSlot(HttpServletRequest request, TimeSlotUpdateRequest timeSlotUpdate) {
		
		UserModel loggedInAdvocate = getLoggedInAdvocate(request);
		
		
		Optional<UserAvailabilityTimeEntity> timeSlotLookup = userAvailabilityTimeRepository.findById(timeSlotUpdate.getTimeSlotUuid());
		
		if(timeSlotLookup.isPresent() && loggedInAdvocate != null) {
			UserAvailabilityTimeEntity timeSlot = timeSlotLookup.get();
			
			
			
			if(StringUtils.equals(timeSlot.getDay().getUser().getUuid(), loggedInAdvocate.getUuid())) {
				timeSlot.setAvailable(timeSlotUpdate.isAvailable());
				timeSlot.setDateUpdated(new Date(System.currentTimeMillis()));
				
				return new UserAvailabilityTimeModel(userAvailabilityTimeRepository.save(timeSlot));	
			}
		}
		
		return new UserAvailabilityTimeModel();
	}
	
	@Transactional
	@Override
	public UserModel initializeDefaultAvailability(String advocateUuid) {

		LOGGER.info("Initializing default schedule for advocate {}", advocateUuid);
		
		Optional<UserEntity> advocateLookup = userRepository.findById(advocateUuid);
		if(advocateLookup.isPresent()) {
			
			UserEntity advocateEntity = advocateLookup.get();
			
			
			
			List<UserAvailabilityDayEntity> availability = createDefaultAvailability(advocateEntity);
			
			for(UserAvailabilityDayEntity element : availability) {
				element.setTimeSlots(createDefaultTimeSlots(element));
			}
			
			advocateEntity.setAvailability(availability);
			
			LOGGER.info("User IS {}", advocateEntity);
			
			userRepository.save(advocateEntity);
			
			return new UserModel(userRepository.save(advocateEntity));
			
		}else {
			return null;
		}
	}
	
	@Transactional
	private List<UserAvailabilityDayEntity> createDefaultAvailability(UserEntity advocate) {
		
		List<String> defaultScheduleDays = messageSourceComponent.getAdvocateScheduleDefaultDaysOfWeek();
		
		List<UserAvailabilityDayEntity> daysAvailable = new ArrayList<>();
		
		long sortOrder = 0;
		for(String scheduleDay : defaultScheduleDays) {
			daysAvailable.add(new UserAvailabilityDayEntity(advocate, scheduleDay, sortOrder++));
		}
		
		return userAvailabilityDayRepository.saveAll(daysAvailable);
	}
	
	@Transactional
	private List<UserAvailabilityTimeEntity> createDefaultTimeSlots(UserAvailabilityDayEntity dayAvailable) {
		
		List<String> defaultBlocksOfTime = messageSourceComponent.getAdvocateScheduleDefaultBlocksOfTime();
		
		List<UserAvailabilityTimeEntity> timeSlots = new ArrayList<>();
		
		long sortOrder = 0;
		for(String blockOfTime : defaultBlocksOfTime) {
			timeSlots.add(new UserAvailabilityTimeEntity(dayAvailable, blockOfTime, sortOrder++));
		}
		
		return userAvailabilityTimeRepository.saveAll(timeSlots);
	}

	@Override
	public UserModel getLoggedInAdvocate(HttpServletRequest request) {
		
		String emailAddress = jwtTokenService.getSubject(request);
		
		Optional<UserEntity> advocateLookup = userRepository.findByEmailAddress(emailAddress);
		LOGGER.info("Advocate lookup found? -- {}", advocateLookup.isPresent());
		
		if(advocateLookup.isPresent()) {
			
			UserEntity loggedInAdvocate = advocateLookup.get();
			
			LOGGER.info("Logged in advocate lookup found. UUID={}", loggedInAdvocate.getUuid());
			return new UserModel(loggedInAdvocate);
		}else {
			return null;
		}
	}
	
	

	
	
}
