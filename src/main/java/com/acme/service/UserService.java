package com.acme.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.acme.exception.RoleNotFoundException;
import com.acme.exception.UserSecurityException;
import com.acme.jpa.entity.UserEntity;
import com.acme.rest.controller.model.UserModel;
import com.acme.rest.controller.model.request.UserPasswordModel;

import jakarta.servlet.http.HttpServletRequest;

public interface UserService extends UserDetailsService{

	UserModel createUser(UserModel userModel) throws RoleNotFoundException;
	
	UserModel validateUserEmail(String uuid);
	

	UserModel getUserByEmailAddress(String emailAddress);
	
	UserEntity updateUser(UserEntity user);
	
	UserModel getLoggedInUser(HttpServletRequest request);

	/**
	 * UserModel object is optional and if provided will 
	 */
	void setUserPassword(HttpServletRequest request, UserPasswordModel userPasswordModel) throws UserSecurityException;
	
	Optional<UserEntity> getAuthenticatedUser(HttpServletRequest request);

	boolean userPhoneNumberExists(String phoneNumber);

	boolean userEmailAddressExists(String emailAddress);
}
