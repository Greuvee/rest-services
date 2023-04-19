package com.acme.integration.meeting.data;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ZoomMeetingRecurrence implements Serializable {

	private static final long serialVersionUID = -5158254888890718662L;

	private Integer type;
    private Integer repeatInterval;
    private Integer monthlyDay;
    private Integer monthlyWeek;
    private Integer monthlyWeekDay;	
    private Integer endTimes;
 
    private String endDateTime;
    private String weeklyDays;

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@JsonProperty("repeat_interval")
	public Integer getRepeatInterval() {
		return repeatInterval;
	}

	@JsonProperty("repeat_interval")
	public void setRepeatInterval(Integer repeatInterval) {
		this.repeatInterval = repeatInterval;
	}

	@JsonProperty("weekly_days")
	public String getWeeklyDays() {
		return weeklyDays;
	}

	@JsonProperty("weekly_days")
	public void setWeeklyDays(String weeklyDays) {
		this.weeklyDays = weeklyDays;
	}

	@JsonProperty("monthly_day")
	public Integer getMonthlyDay() {
		return monthlyDay;
	}

	@JsonProperty("monthly_day")
	public void setMonthlyDay(Integer monthlyDay) {
		this.monthlyDay = monthlyDay;
	}

	@JsonProperty("monthly_week")
	public Integer getMonthlyWeek() {
		return monthlyWeek;
	}

	@JsonProperty("monthly_week")
	public void setMonthlyWeek(Integer monthlyWeek) {
		this.monthlyWeek = monthlyWeek;
	}

	@JsonProperty("monthly_week_day")
	public Integer getMonthlyWeekDay() {
		return monthlyWeekDay;
	}

	@JsonProperty("monthly_week_day")
	public void setMonthlyWeekDay(Integer monthlyWeekDay) {
		this.monthlyWeekDay = monthlyWeekDay;
	}

	@JsonProperty("end_times")
	public Integer getEndTimes() {
		return endTimes;
	}

	@JsonProperty("end_times")
	public void setEndTimes(Integer endTimes) {
		this.endTimes = endTimes;
	}

	@JsonProperty("end_date_time")
	public String getEndDateTime() {
		return endDateTime;
	}

	@JsonProperty("end_date_time")
	public void setEndDateTime(String endDateTime) {
		this.endDateTime = endDateTime;
	}
    
}
