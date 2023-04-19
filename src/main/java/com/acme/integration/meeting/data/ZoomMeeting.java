package com.acme.integration.meeting.data;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
public class ZoomMeeting implements Serializable {

	private static final long serialVersionUID = 9004751968298340264L;

	private Integer pmi;
	private Integer type;
	private Integer duration;
	
	private Long id;
	
	private String uuid;
    private String assistantId;
    private String hostEmail;
    private String registrationUrl;
    private String topic;
    private String startTime;
    private String scheduleFor;
    private String timezone;
    private String createdAt;
    private String password;
    private String agenda;
    private String startUrl;
    private String joinUrl;
    private String h323Password;
    
    private Set<String> recipients;
    
    private ZoomMeetingSettings settings;
    private ZoomMeetingRecurrence recurrence;
    
    private List<ZoomMeetingOccurence> occurrences;
    private List<ZoomMeetingTrackingFields> trackingFields;

    private ZoomMeeting() {
		super();
	}
	
	public ZoomMeeting(String hostEmail, Set<String> recipients) {
		this();
		this.hostEmail = hostEmail;
		this.recipients = recipients;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getPmi() {
		return pmi;
	}

	public void setPmi(Integer pmi) {
		this.pmi = pmi;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@JsonProperty("assistant_id")
	public String getAssistantId() {
		return assistantId;
	}

	@JsonProperty("assistant_id")
	public void setAssistantId(String assistantId) {
		this.assistantId = assistantId;
	}

	@JsonProperty("host_email")
	public String getHostEmail() {
		return hostEmail;
	}

	@JsonProperty("host_email")
	public void setHostEmail(String hostEmail) {
		this.hostEmail = hostEmail;
	}

	@JsonProperty("registration_url")
	public String getRegistrationUrl() {
		return registrationUrl;
	}

	@JsonProperty("registration_url")
	public void setRegistrationUrl(String registrationUrl) {
		this.registrationUrl = registrationUrl;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	@JsonProperty("start_time")
	public String getStartTime() {
		return startTime;
	}

	@JsonProperty("start_time")
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	@JsonProperty("schedule_for")
	public String getScheduleFor() {
		return scheduleFor;
	}

	@JsonProperty("schedule_for")
	public void setScheduleFor(String scheduleFor) {
		this.scheduleFor = scheduleFor;
	}

	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	@JsonProperty("created_at")
	public String getCreatedAt() {
		return createdAt;
	}

	@JsonProperty("created_at")
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAgenda() {
		return agenda;
	}

	public void setAgenda(String agenda) {
		this.agenda = agenda;
	}

	@JsonProperty("start_url")
	public String getStartUrl() {
		return startUrl;
	}

	@JsonProperty("start_url")
	public void setStartUrl(String startUrl) {
		this.startUrl = startUrl;
	}

	@JsonProperty("join_url")
	public String getJoinUrl() {
		return joinUrl;
	}

	@JsonProperty("join_url")
	public void setJoinUrl(String joinUrl) {
		this.joinUrl = joinUrl;
	}

	@JsonProperty("h323_password")
	public String getH323Password() {
		return h323Password;
	}

	@JsonProperty("h323_password")
	public void setH323Password(String h323Password) {
		this.h323Password = h323Password;
	}

	public Set<String> getRecipients() {
		return recipients;
	}

	public void setRecipients(Set<String> recipients) {
		this.recipients = recipients;
	}
	
	public ZoomMeetingSettings getSettings() {
		return settings;
	}

	public void setSettings(ZoomMeetingSettings settings) {
		this.settings = settings;
	}

	public ZoomMeetingRecurrence getRecurrence() {
		return recurrence;
	}

	public void setRecurrence(ZoomMeetingRecurrence recurrence) {
		this.recurrence = recurrence;
	}

	public List<ZoomMeetingOccurence> getOccurrences() {
		return occurrences;
	}

	public void setOccurrences(List<ZoomMeetingOccurence> occurrences) {
		this.occurrences = occurrences;
	}

	@JsonProperty("tracking_fields")
	public List<ZoomMeetingTrackingFields> getTrackingFields() {
		return trackingFields;
	}

	@JsonProperty("tracking_fields")
	public void setTrackingFields(List<ZoomMeetingTrackingFields> trackingFields) {
		this.trackingFields = trackingFields;
	}
    
}
