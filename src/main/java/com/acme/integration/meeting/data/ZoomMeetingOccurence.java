package com.acme.integration.meeting.data;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
public class ZoomMeetingOccurence implements Serializable {

	private static final long serialVersionUID = -7868921588603151018L;

	private Integer duration;
	
	private String occurrenceId;
    private String startTime;
    private String status;

    @JsonProperty("occurrence_id")
	public String getOccurrenceId() {
		return occurrenceId;
	}

    @JsonProperty("occurrence_id")
	public void setOccurrenceId(String occurrenceId) {
		this.occurrenceId = occurrenceId;
	}

    @JsonProperty("start_time")
	public String getStartTime() {
		return startTime;
	}

    @JsonProperty("start_time")
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
    
}
