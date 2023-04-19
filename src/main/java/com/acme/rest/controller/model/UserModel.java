package com.acme.rest.controller.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acme.jpa.entity.RoleEntity;
import com.acme.jpa.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true, value = { "roles" })
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserModel {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserModel.class);

	private String uuid;
	
	@NotBlank
	private String firstName;
	
	@NotBlank
	private String lastName;
	
	@NotBlank @Email
	private String emailAddress;
	
	@NotBlank
	private String phoneNumber;
	
	private boolean emailAddressValidated = false;
	
	private String gender;
	
	private String location;
	
	private String timezone;
	
	private boolean enabled;
	
	private boolean accountExpired;
	
	private boolean accountLocked;
	
	private boolean credentialsExpired;
	
	private Date dateCreated;
	
	private Date dateModified;
	
	private List<String> roles;
	
	private List<UserAvailabilityDayModel> availability;
	
	private List<MeetingModel> meetings;
	
	private String message;
	
	public UserModel() {
		super();
	}
	
	public UserModel(String message) {
		this();
		setMessage(message);
	}
	
	public UserModel(@NotNull UserEntity user) {
		if(user != null) {
			setUuid(user.getUuid());
			setFirstName(user.getFirstName());
			setLastName(user.getLastName());
			setEmailAddress(user.getEmailAddress());
			setPhoneNumber(user.getPhoneNumber());
			setEnabled(user.isEnabled());
			setDateCreated(user.getDateCreated());
			setDateModified(user.getDateModified());
			setGender(user.getGender());
			setLocation(user.getLocation());
			setTimezone(user.getTimezone());
			setAccountExpired(BooleanUtils.isFalse(user.isAccountNonExpired()));
			setCredentialsExpired(BooleanUtils.isFalse(user.isCredentialsNonExpired()));
			setAccountLocked(BooleanUtils.isFalse(user.isAccountNonLocked()));
			
			if(user.getEmailValidation() != null) {
				setEmailAddressValidated(user.getEmailValidation().isValidated());
			}
			
			if(user.getRoles() != null) {
				setRoles(user.getRoles().stream().map(RoleEntity::getName).toList());
			}
			
			if(CollectionUtils.isNotEmpty(user.getAvailability())) {
				user.getAvailability()
					.stream()
					.filter(Objects::nonNull)
					.forEach(e -> addAvailability(new UserAvailabilityDayModel(e)));
			}
			
			if(CollectionUtils.isNotEmpty(user.getMeetings())) {
				LOGGER.info("Populating meetings - user has {} elements", user.getMeetings().size());
				setMeetings(
				user.getMeetings()
					.stream()
					.filter(Objects::nonNull)
					.map(e -> new MeetingModel(e, "scheduled", true))
					.toList()
				);
			}
		}
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		if(firstName != null) {
			this.firstName = firstName.trim();
		}
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		if(lastName != null) {
			this.lastName = lastName.trim();
		}
	}

	public String getEmailAddress() {
		return this.emailAddress;
	}

	public void setEmailAddress(@Email String emailAddress) {
		if(emailAddress != null) {
			this.emailAddress = emailAddress.trim();
		}
	}

	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		if(phoneNumber != null) {
			this.phoneNumber = phoneNumber.replaceAll("[\\D]", "");
		}
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getDateModified() {
		return dateModified;
	}

	public void setDateModified(Date dateModified) {
		this.dateModified = dateModified;
	}

	public String getUuid() {
		return this.uuid;
	}

	public void setUuid(String uuid) {
		if(uuid != null) {
			this.uuid = uuid.trim();
		}
	}

	public boolean isEmailAddressValidated() {
		return emailAddressValidated;
	}

	public void setEmailAddressValidated(boolean emailAddressValidated) {
		this.emailAddressValidated = emailAddressValidated;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		if(gender != null) {
			this.gender = gender.trim();
		}
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		if(location != null) {
			this.location = location.trim();
		}
	}

	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		if(timezone != null) {
			this.timezone = timezone.trim();
		}
	}

	public boolean isAccountExpired() {
		return accountExpired;
	}

	public void setAccountExpired(boolean accountExpired) {
		this.accountExpired = accountExpired;
	}

	public boolean isAccountLocked() {
		return accountLocked;
	}

	public void setAccountLocked(boolean accountLocked) {
		this.accountLocked = accountLocked;
	}

	public boolean isCredentialsExpired() {
		return credentialsExpired;
	}

	public void setCredentialsExpired(boolean credentialsExpired) {
		this.credentialsExpired = credentialsExpired;
	}

	public List<UserAvailabilityDayModel> getAvailability() {
		if(this.availability == null || this.availability.isEmpty()) {
			return Collections.emptyList();
		}else {
			return this.availability
				.stream()
				.filter(Objects::nonNull)
				.sorted( (UserAvailabilityDayModel o1, UserAvailabilityDayModel o2) -> 
					o1 != null && o2 != null ? Long.compare(o1.getSortOrder(), o2.getSortOrder()) : 0
				)
				.toList();
		}	
	}
	
	private void addAvailability(UserAvailabilityDayModel element) {
		if(this.availability == null) {
			this.availability = new ArrayList<>();
		}
		this.availability.add(element);
	}

	public List<MeetingModel> getMeetings() {
		return meetings;
	}

	public void setMeetings(List<MeetingModel> meetings) {
		this.meetings = meetings;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}	
}
