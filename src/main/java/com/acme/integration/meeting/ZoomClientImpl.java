package com.acme.integration.meeting;

import java.security.Key;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.collections4.SetUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.acme.exception.MeetingRequestException;
import com.acme.integration.meeting.data.ZoomMeeting;
import com.acme.integration.meeting.data.ZoomMeetingSettings;
import com.acme.jpa.entity.UserEntity;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class ZoomClientImpl implements ZoomClient {

	private static final String API_URL = "https://api.zoom.us/v2/users/%s/meetings";
	
	@Value("${zoom.api.userid}")
	private String zoomUserId;
	
	@Value("${zoom.api.password}")
	private String zoomPassword;
	
	@Value("${zoom.api.secret}")
	private String zoomApiSecret;
	
	@Value("${zoom.api.key}")
	private String zoomApiKey;
	
	@Value("${zoom.meeting.default.topic}")
	private String defaultTopic;
	
	@Value("${zoom.meeting.default.agenda}")
	private String defaultAgenda;
	
	@Value("${zoom.meeting.default.duration}")
	private int defaultDuration;
	
	@Override
	public ZoomMeeting createMeeting(UserEntity hostUser, String... recipients) throws MeetingRequestException {
		
		ZoomMeeting meetingRequest = new ZoomMeeting(hostUser.getEmailAddress(), SetUtils.unmodifiableSet(recipients));
		meetingRequest.setPassword(generateMeetingPassword());
		meetingRequest.setTopic(defaultTopic);
		meetingRequest.setAgenda(defaultAgenda);
		meetingRequest.setDuration(defaultDuration);
		meetingRequest.setSettings(getDefaultMeetingSettings());
		
		return submitMeetingRequest(meetingRequest);
	}
	
	private ZoomMeeting submitMeetingRequest(ZoomMeeting meetingRequest) throws MeetingRequestException {
		RestTemplate restTemplate = new RestTemplate();
        
        HttpEntity<ZoomMeeting> httpEntity = new HttpEntity<>(meetingRequest, getRequestHeaders());
        
        ResponseEntity<ZoomMeeting> meetingResponseEntity = restTemplate.exchange(getApiUrl(), HttpMethod.POST, httpEntity, ZoomMeeting.class);
        
        if(meetingResponseEntity.getStatusCode().is2xxSuccessful()) {
            return meetingResponseEntity.getBody();
        } else {
        	throw new MeetingRequestException(String.format("Error while creating zoom meeting: %s", meetingResponseEntity.getStatusCode()));
        }
	}
	
	private ZoomMeetingSettings getDefaultMeetingSettings() {
		ZoomMeetingSettings meetingRequestSettings = new ZoomMeetingSettings();
		meetingRequestSettings.setJoinBeforeHost(false);
		meetingRequestSettings.setParticipantVideo(true);
		meetingRequestSettings.setHostVideo(true);
		meetingRequestSettings.setAutoRecording("cloud");
		meetingRequestSettings.setMuteUponEntry(true);
		meetingRequestSettings.setWaitingRoom(true);
		
		return meetingRequestSettings;
	}
	
	private HttpHeaders getRequestHeaders() {
		HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + generateZoomJWTTOken());
        headers.add("content-type", "application/json");
        
        return headers;
	}
	
	/**
     * Generate JWT token for Zoom using api credentials
     * 
     * @return JWT Token String
     */
    private String generateZoomJWTTOken() {
        String id = UUID.randomUUID().toString().replace("-", "");
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        Date creation = new Date(System.currentTimeMillis());
        Date tokenExpiry = new Date(System.currentTimeMillis() + (1000 * 60));

        Key key = Keys
            .hmacShaKeyFor(zoomApiSecret.getBytes());
        return Jwts.builder()
            .setId(id)
            .setIssuer(zoomApiKey)
            .setIssuedAt(creation)
            .setSubject("")
            .setExpiration(tokenExpiry)
            .signWith(key, signatureAlgorithm)
            .compact();
    }
    
    private String getApiUrl() {
    	return String.format(API_URL, zoomUserId);
    }
	
	private String generateMeetingPassword() {
		return UUID.randomUUID().toString().replace("-", "").substring(0, 9);
	}
	
}
