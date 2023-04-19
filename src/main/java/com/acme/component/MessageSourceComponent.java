package com.acme.component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MessageSourceComponent {

	private static final Logger LOGGER = LoggerFactory.getLogger(MessageSourceComponent.class);
	
	@Value("${advocate.schedule.default.days.of.week}")
	private String defaultDaysOfWeek;
	
	@Value("${advocate.schedule.default.hours.of.day}")
	private String defaultHoursOfDay;
	
	public List<String> getAdvocateScheduleDefaultDaysOfWeek(){
		
		if(StringUtils.isNotEmpty(defaultDaysOfWeek)) {
			LOGGER.debug("Loaded advocate schedule default days of week: {}", defaultDaysOfWeek);
			return Arrays.asList(defaultDaysOfWeek.split(",")).stream().collect(Collectors.toList());
		}else {
			return new ArrayList<>();
		}
	}
	
	public List<String> getAdvocateScheduleDefaultBlocksOfTime(){
		
		if(StringUtils.isNotEmpty(defaultHoursOfDay)) {
			LOGGER.debug("Loaded advocate schedule default hours of day: {}", defaultHoursOfDay);
			return Arrays.asList(defaultHoursOfDay.split(",")).stream().collect(Collectors.toList());
		}else {
			return new ArrayList<>();
		}
	}
}
