package com.acme.integration.meeting.data;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ZoomMeetingSettings implements Serializable {

	private static final long serialVersionUID = -3151620596331830650L;

    private Integer approvalType;
    private Integer registrationType;

    private Boolean hostVideo;
    private Boolean participantVideo;
    private Boolean cnMeeting;
    private Boolean inMeeting;
    private Boolean joinBeforeHost;
    private Boolean muteUponEntry;
    private Boolean watermark;
    private Boolean usePmi;
    private Boolean closeRegistration;
    private Boolean waitingRoom;
    private Boolean registrantsEmailNotification;
    private Boolean registrantsConfirmationEmail;
    private Boolean meetingAuthentication;
    private Boolean showShareButton;
    private Boolean allowMultipleDevices;
    
    private String audio;
    private String autoRecording;
    private String alternativeHosts;
    private String contactName;
    private String contactEmail;
    private String authenticationOption;
    private String authenticatedDomains;
    private String authenticationName;
    private String encryptionType;

    private List<String> globalDialInCountries;    
    private List<ZoomMeetingDialInNumbers> globalDialInNumbers;

    @JsonProperty("approval_type")
	public Integer getApprovalType() {
		return approvalType;
	}

    @JsonProperty("approval_type")
	public void setApprovalType(Integer approvalType) {
		this.approvalType = approvalType;
	}

    @JsonProperty("registration_type")
	public Integer getRegistrationType() {
		return registrationType;
	}

    @JsonProperty("registration_type")
	public void setRegistrationType(Integer registrationType) {
		this.registrationType = registrationType;
	}

    @JsonProperty("host_video")
	public Boolean isHostVideo() {
		return hostVideo;
	}

    @JsonProperty("host_video")
	public void setHostVideo(Boolean hostVideo) {
		this.hostVideo = hostVideo;
	}

    @JsonProperty("participant_video")
	public Boolean isParticipantVideo() {
		return participantVideo;
	}

    @JsonProperty("participant_video")
	public void setParticipantVideo(Boolean participantVideo) {
		this.participantVideo = participantVideo;
	}

    @JsonProperty("cn_meeting")
	public Boolean isCnMeeting() {
		return cnMeeting;
	}

    @JsonProperty("cn_meeting")
	public void setCnMeeting(Boolean cnMeeting) {
		this.cnMeeting = cnMeeting;
	}

    @JsonProperty("in_meeting")
	public Boolean isInMeeting() {
		return inMeeting;
	}

    @JsonProperty("in_meeting")
	public void setInMeeting(Boolean inMeeting) {
		this.inMeeting = inMeeting;
	}

    @JsonProperty("join_before_host")
	public Boolean isJoinBeforeHost() {
		return joinBeforeHost;
	}

    @JsonProperty("join_before_host")
	public void setJoinBeforeHost(Boolean joinBeforeHost) {
		this.joinBeforeHost = joinBeforeHost;
	}

    @JsonProperty("mute_upon_entry")
	public Boolean isMuteUponEntry() {
		return muteUponEntry;
	}

    @JsonProperty("mute_upon_entry")
	public void setMuteUponEntry(Boolean muteUponEntry) {
		this.muteUponEntry = muteUponEntry;
	}

	public Boolean isWatermark() {
		return watermark;
	}

	public void setWatermark(Boolean watermark) {
		this.watermark = watermark;
	}

	@JsonProperty("use_pmi")
	public Boolean isUsePmi() {
		return usePmi;
	}

	@JsonProperty("use_pmi")
	public void setUsePmi(Boolean usePmi) {
		this.usePmi = usePmi;
	}

	@JsonProperty("close_registration")
	public Boolean isCloseRegistration() {
		return closeRegistration;
	}

	@JsonProperty("close_registration")
	public void setCloseRegistration(Boolean closeRegistration) {
		this.closeRegistration = closeRegistration;
	}

	@JsonProperty("waiting_room")
	public Boolean isWaitingRoom() {
		return waitingRoom;
	}

	@JsonProperty("waiting_room")
	public void setWaitingRoom(Boolean waitingRoom) {
		this.waitingRoom = waitingRoom;
	}

	@JsonProperty("registrants_email_notification")
	public Boolean isRegistrantsEmailNotification() {
		return registrantsEmailNotification;
	}

	@JsonProperty("registrants_email_notification")
	public void setRegistrantsEmailNotification(Boolean registrantsEmailNotification) {
		this.registrantsEmailNotification = registrantsEmailNotification;
	}

	@JsonProperty("registrants_confirmation_email")
	public Boolean isRegistrantsConfirmationEmail() {
		return registrantsConfirmationEmail;
	}

	@JsonProperty("registrants_confirmation_email")
	public void setRegistrantsConfirmationEmail(Boolean registrantsConfirmationEmail) {
		this.registrantsConfirmationEmail = registrantsConfirmationEmail;
	}

	@JsonProperty("meeting_authentication")
	public Boolean isMeetingAuthentication() {
		return meetingAuthentication;
	}

	@JsonProperty("meeting_authentication")
	public void setMeetingAuthentication(Boolean meetingAuthentication) {
		this.meetingAuthentication = meetingAuthentication;
	}

	@JsonProperty("show_share_button")
	public Boolean isShowShareButton() {
		return showShareButton;
	}

	@JsonProperty("show_share_button")
	public void setShowShareButton(Boolean showShareButton) {
		this.showShareButton = showShareButton;
	}

	@JsonProperty("allow_multiple_devices")
	public Boolean isAllowMultipleDevices() {
		return allowMultipleDevices;
	}

	@JsonProperty("allow_multiple_devices")
	public void setAllowMultipleDevices(Boolean allowMultipleDevices) {
		this.allowMultipleDevices = allowMultipleDevices;
	}

	public String getAudio() {
		return audio;
	}

	public void setAudio(String audio) {
		this.audio = audio;
	}

	@JsonProperty("auto_recording")
	public String getAutoRecording() {
		return autoRecording;
	}

	@JsonProperty("auto_recording")
	public void setAutoRecording(String autoRecording) {
		this.autoRecording = autoRecording;
	}

	@JsonProperty("alternative_hosts")
	public String getAlternativeHosts() {
		return alternativeHosts;
	}

	@JsonProperty("alternative_hosts")
	public void setAlternativeHosts(String alternativeHosts) {
		this.alternativeHosts = alternativeHosts;
	}

	@JsonProperty("contact_name")
	public String getContactName() {
		return contactName;
	}

	@JsonProperty("contact_name")
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	@JsonProperty("contact_email")
	public String getContactEmail() {
		return contactEmail;
	}

	@JsonProperty("contact_email")
	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	@JsonProperty("authentication_option")
	public String getAuthenticationOption() {
		return authenticationOption;
	}

	@JsonProperty("authentication_option")
	public void setAuthenticationOption(String authenticationOption) {
		this.authenticationOption = authenticationOption;
	}

	@JsonProperty("authenticated_domains")
	public String getAuthenticatedDomains() {
		return authenticatedDomains;
	}

	@JsonProperty("authenticated_domains")
	public void setAuthenticatedDomains(String authenticatedDomains) {
		this.authenticatedDomains = authenticatedDomains;
	}

	@JsonProperty("authentication_name")
	public String getAuthenticationName() {
		return authenticationName;
	}

	@JsonProperty("authentication_name")
	public void setAuthenticationName(String authenticationName) {
		this.authenticationName = authenticationName;
	}

	@JsonProperty("encryption_type")
	public String getEncryptionType() {
		return encryptionType;
	}

	@JsonProperty("encryption_type")
	public void setEncryptionType(String encryptionType) {
		this.encryptionType = encryptionType;
	}

	@JsonProperty("global_dial_in_countries")
	public List<String> getGlobalDialInCountries() {
		return globalDialInCountries;
	}

	@JsonProperty("global_dial_in_countries")
	public void setGlobalDialInCountries(List<String> globalDialInCountries) {
		this.globalDialInCountries = globalDialInCountries;
	}

	@JsonProperty("global_dial_in_numbers")
	public List<ZoomMeetingDialInNumbers> getGlobalDialInNumbers() {
		return globalDialInNumbers;
	}

	@JsonProperty("global_dial_in_numbers")
	public void setGlobalDialInNumbers(List<ZoomMeetingDialInNumbers> globalDialInNumbers) {
		this.globalDialInNumbers = globalDialInNumbers;
	}
    
}
