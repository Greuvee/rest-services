package com.acme.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.acme.ApiApplication;
import com.acme.SecurityConfig;
import com.acme.exception.RoleNotFoundException;
import com.acme.exception.UserSecurityException;
import com.acme.jpa.entity.RoleEntity;
import com.acme.jpa.entity.UserEntity;
import com.acme.jpa.entity.UserValidationEntity;
import com.acme.jpa.repo.RoleRepository;
import com.acme.jpa.repo.UserRepository;
import com.acme.jpa.repo.UserValidationRepository;
import com.acme.rest.controller.model.UserModel;
import com.acme.rest.controller.model.request.UserPasswordModel;
import com.acme.security.JwtTokenService;
import com.acme.security.JwtTokenServiceImpl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserValidationRepository userValidationRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private EmailService emailService;

	@Autowired
	private JwtTokenService jwtTokenService;

	@Override
	public UserModel createUser(@NotNull UserModel userModel) throws RoleNotFoundException {

		UserEntity newUser = new UserEntity(userModel);
		newUser.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
		newUser.setPasswordExpired(true);
		newUser.setAccountExpired(false);
		newUser.setAccountLocked(false);
		newUser.setEnabled(true);
		newUser.setDateCreated(new Date(System.currentTimeMillis()));

		newUser = userRepository.save(newUser);

		// Add reference entities
		newUser.setEmailValidation(createEmailValidation(newUser));
		newUser.addRole(createRole(newUser));

		newUser = userRepository.save(newUser);

		return new UserModel(newUser);
	}
	
	@Override
	public boolean userEmailAddressExists(String emailAddress) {
		return StringUtils.isNotBlank(emailAddress) && userRepository.findByEmailAddress(emailAddress).isPresent();
	}
	
	@Override
	public boolean userPhoneNumberExists(String phoneNumber) {
		return StringUtils.isNotBlank(phoneNumber) && userRepository.findByPhoneNumber(phoneNumber).isPresent();
	}

	private RoleEntity createRole(UserEntity userEntity) throws RoleNotFoundException {
		Optional<RoleEntity> userRoleLookup = roleRepository.findByName(SecurityConfig.ROLE_USER);
		if (userRoleLookup.isEmpty()) {
			LOGGER.error("Role {} was not found during lookup - Cannot create user {}", SecurityConfig.ROLE_USER,
					userEntity.getEmailAddress());
			throw new RoleNotFoundException(
					String.format("Role %s was not found and is required to create a user", SecurityConfig.ROLE_USER));
		} else {

			RoleEntity userRole = userRoleLookup.get();
			userRole.addUser(userEntity);

			return roleRepository.save(userRole);
		}
	}
	
	@Override
	public Optional<UserEntity> getAuthenticatedUser(HttpServletRequest request) {
		JwtTokenServiceImpl svc = ApiApplication.getApplicationContext().getBean(JwtTokenServiceImpl.class);
		return userRepository.findByEmailAddress(svc.getSubject(request));
	}

	private UserValidationEntity createEmailValidation(UserEntity userEntity) {

		UserValidationEntity emailValidation = new UserValidationEntity();
		emailValidation.setType("email");
		emailValidation.setValidated(false);
		emailValidation.setUser(userEntity);

		try {
			emailValidation.setToken(emailService.sendValidationEmail(new UserModel(userEntity)));
			emailValidation.setDateSent(new Date(System.currentTimeMillis()));
		} catch (Exception e) {
			emailValidation.setDateSent(null);
		}

		return userValidationRepository.save(emailValidation);
	}

	/**
	 * UserModel object is optional and if provided will 
	 */
	@Override
	public void setUserPassword(HttpServletRequest request, UserPasswordModel userPasswordModel) throws UserSecurityException {

		Optional<UserEntity> userEntity;
		
		UserModel loggedInUser = getLoggedInUser(request);
		if(loggedInUser != null) {
			LOGGER.info("User {} is currently logged for updating password", loggedInUser.getUuid());
			userEntity = userRepository.findById(loggedInUser.getUuid());
		
			if(userEntity.isPresent()) {
				UserEntity user = userEntity.get();
				
				if(StringUtils.equalsIgnoreCase(user.getEmailAddress(), userPasswordModel.getEmailAddress()) && StringUtils.isNotBlank(userPasswordModel.getPassword())) {
					user.setPassword(passwordEncoder.encode(userPasswordModel.getPassword()));
					user.setPasswordExpired(false);
					userRepository.save(user);
				}
			}
		}else {
			
			userEntity = userRepository.findByEmailAddress(userPasswordModel.getEmailAddress());
			
			if(userEntity.isPresent()) {
				UserEntity user = userEntity.get();
				LOGGER.info("User {} is not logged in for updating password", user.getUuid());
				
				UserValidationEntity emailValidation = user.getEmailValidation();
				
				if(emailValidation.isValidated() && StringUtils.equalsIgnoreCase(user.getEmailAddress(), userPasswordModel.getEmailAddress()) && StringUtils.isNotBlank(userPasswordModel.getPassword())) {
					user.setPassword(passwordEncoder.encode(userPasswordModel.getPassword()));
					userRepository.save(user);

					emailValidation.setToken(null);
					userValidationRepository.save(emailValidation);
				}
			}
		}
	}
	
	@Override
	public UserModel getUserByEmailAddress(String emailAddress) {
		Optional<UserEntity> userLookup = userRepository.findByEmailAddress(emailAddress != null ? emailAddress.trim() : StringUtils.EMPTY);
		if (userLookup.isPresent()) {
			return new UserModel(userLookup.get());
		} else {
			return null;
		}
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Optional<UserEntity> userLookup = userRepository.findByEmailAddress(username != null ? username.trim() : StringUtils.EMPTY);

		if (userLookup.isEmpty()) {
			userLookup = userRepository.findByPhoneNumber(username != null ? username.trim() : StringUtils.EMPTY);
		}
		
		if (userLookup.isEmpty()) {
			
			Optional<RoleEntity> userRole = roleRepository.findByName(SecurityConfig.ROLE_USER);
			
            return new org.springframework.security.core.userdetails.User(
              " ", " ", true, true, true, true, 
              getAuthorities(Arrays.asList(userRole.isPresent() ? userRole.get() : null)));
        }else {
        	
        	UserEntity user = userLookup.get();
        	
        	return new org.springframework.security.core.userdetails.User(
        	          user.getEmailAddress(), user.getPassword(), user.isEnabled(), true, true, 
        	          true, getAuthorities(user.getRoles()));
        }

	}

	private Collection<? extends GrantedAuthority> getAuthorities(Collection<RoleEntity> roles) {

		return getGrantedAuthorities(getPrivileges(roles));
	}

	private List<String> getPrivileges(Collection<RoleEntity> roles) {

		List<String> privileges = new ArrayList<>();
		
		for (RoleEntity role : roles) {
			privileges.add(role.getName());
		}
		
		return privileges;
	}

	private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
		List<GrantedAuthority> authorities = new ArrayList<>();
		for (String privilege : privileges) {
			authorities.add(new SimpleGrantedAuthority(privilege));
		}
		return authorities;
	}

	@Override
	public UserModel validateUserEmail(@NotBlank String uuid) {

		Optional<UserEntity> userEntityOptional = userRepository.findById(uuid != null ? uuid.trim() : StringUtils.EMPTY);

		if (userEntityOptional.isPresent()) {
			UserEntity userEntity = userEntityOptional.get();

			UserValidationEntity emailValidation = null;

			if (userEntity.getEmailValidation() != null) {
				emailValidation = userEntity.getEmailValidation();
			} else {
				emailValidation = new UserValidationEntity();
				emailValidation.setDateSent(new Date(System.currentTimeMillis()));
				emailValidation.setType("email");
				emailValidation.setUser(userEntity);
			}

			if (StringUtils.equals(userEntity.getUuid(), uuid)) {
				emailValidation.setValidated(true);
			}

			userEntity.setEmailValidation(userValidationRepository.save(emailValidation));

			return new UserModel(userRepository.save(userEntity));
		} else {
			return null;
		}
	}

	@Override
	public UserEntity updateUser(UserEntity user) {
		
		if(user != null) {
			user.setDateModified(new Date(System.currentTimeMillis()));
			return userRepository.save(user);
		}else {
			return null;
		}
		
	}

	@Override
	public UserModel getLoggedInUser(HttpServletRequest request) {
		
		String emailAddress = jwtTokenService.getSubject(request);
		
		LOGGER.info("Getting logged in user with email {}", emailAddress);
		
		Optional<UserEntity> userLookup = userRepository.findByEmailAddress(emailAddress != null ? emailAddress.trim() : StringUtils.EMPTY);
		if(userLookup.isPresent()) {
			
			UserEntity loggedInUser = userLookup.get();
			
			LOGGER.info("Logged in user lookup found. UUID={}", loggedInUser.getUuid());
			return new UserModel(loggedInUser);
		}else {
			return null;
		}
	}
}
