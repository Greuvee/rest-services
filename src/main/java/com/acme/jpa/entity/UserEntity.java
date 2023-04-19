package com.acme.jpa.entity;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.acme.rest.controller.model.UserModel;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "users")
public class UserEntity implements UserDetails {

	private static final long serialVersionUID = 5261921741105446764L;

	@Id @GeneratedValue(strategy = GenerationType.UUID)
    private String uuid; 
	
	@Column(nullable = false) @NotBlank
	private String firstName;
	
	@Column(nullable = false) @NotBlank
	private String lastName;
	
	@Column(nullable = false, unique = true) @Email
	private String emailAddress;
	
	@Column(nullable = false, unique = true) @Length(min = 10, max = 10) @Pattern(regexp="(^$|[0-9]{10})")
	private String phoneNumber;
	
	@Column(nullable = false) @NotBlank
	private String password;
	
	@Column
	private boolean enabled = false;
	
	@Column
	private boolean accountExpired = false;
	
	@Column
	private boolean accountLocked = false;
	
	@Column
	private boolean passwordExpired = false;
	
	@Column
	private String gender;
	
	@Column
	private String location;
	
	@Column
	private String timezone;
	
	@OneToOne
	private UserValidationEntity emailValidation;
	
	@OneToOne
	private UserValidationEntity smsValidation;
	
	@Column
	private Date dateCreated;
	
	@Column
	private Date dateModified;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="user", fetch = FetchType.EAGER)
	private List<UserAvailabilityDayEntity> availability;
	
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name="user_meetings")
	private List<MeetingEntity> meetings;
	
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles") 
    private List<RoleEntity> roles;

	public UserEntity() {
		super();
	}
	
	public UserEntity(UserModel user) {
		if(user != null) {
			setFirstName(user.getFirstName());
			setLastName(user.getLastName());
			setEmailAddress(user.getEmailAddress());
			setPhoneNumber(user.getPhoneNumber());
			setGender(user.getGender());
			setLocation(user.getLocation());
			setTimezone(user.getTimezone());
		}
	}
	
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		if(uuid != null) {
			this.uuid = uuid.trim();
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
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		if(emailAddress != null) {
			this.emailAddress = emailAddress.trim();
		}
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(@NotEmpty String phoneNumber) {
		if(phoneNumber != null) {
			this.phoneNumber = phoneNumber.replaceAll("[\\D]", "");
		}
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	@Override
	public String getUsername() {
		return this.emailAddress;
	}

	@Override
	public boolean isAccountNonExpired() {
		return BooleanUtils.isFalse(accountExpired);
	}

	@Override
	public boolean isAccountNonLocked() {
		return BooleanUtils.isFalse(accountLocked);
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return BooleanUtils.isFalse(passwordExpired);
	}

	public Collection<RoleEntity> getRoles() {
		if(this.roles == null) {
			this.roles = new ArrayList<>();
		}
		return this.roles;
	}

	public void setRoles(List<RoleEntity> roles) {
		if(roles == null) {
			this.roles = new ArrayList<>();
		}else {
			this.roles = roles;
		}
	}
	
	public void addRole(RoleEntity roleEntity) {
		getRoles().add(roleEntity);
	}
	
	public UserValidationEntity getEmailValidation() {
		return emailValidation;
	}

	public void setEmailValidation(UserValidationEntity emailValidation) {
		this.emailValidation = emailValidation;
	}

	public UserValidationEntity getSmsValidation() {
		return smsValidation;
	}

	public void setSmsValidation(UserValidationEntity smsValidation) {
		this.smsValidation = smsValidation;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).toList();
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

	public void setAccountExpired(boolean accountExpired) {
		this.accountExpired = accountExpired;
	}

	public void setAccountLocked(boolean accountLocked) {
		this.accountLocked = accountLocked;
	}

	public void setPasswordExpired(boolean passwordExpired) {
		this.passwordExpired = passwordExpired;
	}
	
	public List<UserAvailabilityDayEntity> getAvailability() {
		if(this.availability == null) {
			this.availability = new ArrayList<>();
		}
		
		return this.availability
			.stream()
			.filter(Objects::nonNull)
			.sorted( (UserAvailabilityDayEntity o1, UserAvailabilityDayEntity o2) -> 
				o1 != null && o2 != null ? Long.compare(o1.getSortOrder(), o2.getSortOrder()) : 0 
			)
			.toList();
	}

	public void setAvailability(List<UserAvailabilityDayEntity> availability) {
		if(CollectionUtils.isNotEmpty(availability)) {
			this.availability = availability;
		}
	}	
	
	@Override
	public String toString() {
		
		 return String.format("UserEntity["
				+ "uuid = %s, "
				+ "firstName = %s, "
				+ "lastName = %s, "
				+ "emailAddress = %s, "
				+ "phoneNumber = %s, "
				+ "password = %s, "
				+ "enabled = %s, "
				+ "accountExpired = %s, "
				+ "accountLocked = %s, "
				+ "passwordExpired = %s, "
				+ "gender = %s, "
				+ "location = %s, "
				+ "timezone = %s, "
				+ "emailValidation = %s, "
				+ "smsValidation = %s, "
				+ "dateCreated = %s, "
				+ "dateModified = %s, "
				+ "daysAvailable = %s, "
				+ "roles = %s, "
				+ "]", 
				getUuid(),  
				getFirstName(), 
				getLastName(), 
				getEmailAddress(), 
				getPhoneNumber(), 
				getPassword(), 
				isEnabled(), 
				isAccountNonExpired(), 
				isAccountNonLocked(), 
				isCredentialsNonExpired(),
				getGender(), 
				getLocation(), 
				getTimezone(), 
				getEmailValidation(), 
				getSmsValidation(), 
				getDateCreated(), 
				getDateModified(), 
				getAvailability(), 
				getRoles());
	}

	public List<MeetingEntity> getMeetings() {
		if(this.meetings == null) {
			this.meetings = new ArrayList<>();
		}
		return meetings;
	}

	public void setMeetings(List<MeetingEntity> meetings) {
		this.meetings = meetings;
	}
	
	public void addMeeting(MeetingEntity meeting) {
		if(this.meetings == null) {
			this.meetings = new ArrayList<>();
		}
		this.meetings.add(meeting);
	}
	
}
