package com.acme.integration.meeting;

import com.acme.exception.MeetingRequestException;
import com.acme.integration.meeting.data.ZoomMeeting;
import com.acme.jpa.entity.UserEntity;

public interface ZoomClient {

	ZoomMeeting createMeeting(UserEntity hostUser, String... recipients) throws MeetingRequestException;
}
