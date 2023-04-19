package com.acme.rest.controller;

import java.net.URI;
import java.util.Base64;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acme.exception.RoleNotFoundException;
import com.acme.rest.controller.model.UserModel;
import com.acme.rest.controller.model.request.UserPasswordModel;
import com.acme.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/register")
public class RegisterController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RegisterController.class);
	
	@Autowired 
	private UserService userService;
	
	@PostMapping
	public ResponseEntity<UserModel> create(@RequestBody UserModel user) throws RoleNotFoundException{
		
		if(user == null) {
			return ResponseEntity.badRequest().body(new UserModel("Request object is required"));
		}else if(StringUtils.isBlank(user.getFirstName()) || StringUtils.isBlank(user.getLastName())) {
			user.setMessage("First and last name are required");
			return ResponseEntity.badRequest().body(user);
		}else if(StringUtils.isBlank(user.getEmailAddress())) {
			user.setMessage("Email address is required");
			return ResponseEntity.badRequest().body(user);
		}else if(StringUtils.isBlank(user.getPhoneNumber())) {
			user.setMessage("Phone number is required");
			return ResponseEntity.badRequest().body(user);
		}else {
			try {
				if(userService.userEmailAddressExists(user.getEmailAddress())) {
					user.setMessage("Email address already exists");
					return ResponseEntity.badRequest().body(user);
				}else if(userService.userPhoneNumberExists(user.getPhoneNumber())) {
					user.setMessage("Phone number already exists");
					return ResponseEntity.badRequest().body(user);
				}else {
					UserModel userModel = userService.createUser(user);
					return ResponseEntity.created(URI.create("/users/" + userModel.getUuid())).body(userModel);
				}
			}catch(Exception e) {
				LOGGER.error("Exception creating user", e);
				user.setMessage(String.format("Error creating user: %s", e.getMessage()));
				return ResponseEntity.internalServerError().body(user);
			}
		}
	}
	
	@GetMapping("/validate/{type}/{token}")
	public ResponseEntity<UserModel> validateUser(@PathVariable String type, @PathVariable String token) {
		
		String decodedToken = new String(Base64.getUrlDecoder().decode(token));
		
		UserModel userModel = null;
		
		if(StringUtils.isEmpty(type)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}else {
			if(StringUtils.equals(type, "email")) {
				LOGGER.info("Email validation attempted for token {}", token);
				userModel = userService.validateUserEmail(decodedToken);
			}else if(StringUtils.equals(type, "sms")) {
				LOGGER.info("SMS validation attempted for token {}", token);
			}
			
			return ResponseEntity.ok(userModel);
		}
	}
	
	@PostMapping("/password")
	public ResponseEntity<UserPasswordModel> setPassword(HttpServletRequest request, @RequestBody UserPasswordModel setPasswordModel) {
		
		UserPasswordModel response = new UserPasswordModel();
		
		try {
			
			if(setPasswordModel == null) {
				response.setMessage("Request object is required to update password");
				return ResponseEntity.badRequest().body(response);
			}else {
				response.setEmailAddress(setPasswordModel.getEmailAddress());
				response.setToken(setPasswordModel.getToken());
				
				if(StringUtils.isBlank(setPasswordModel.getEmailAddress())) {
					response.setMessage("Email address is required to update password");
					return ResponseEntity.badRequest().body(response);
				}else if(StringUtils.isBlank(setPasswordModel.getPassword())) {
					response.setMessage("Password is required to update password");
					return ResponseEntity.badRequest().body(response);
				}else {
					if(StringUtils.isBlank(setPasswordModel.getToken())) {
						userService.setUserPassword(request, setPasswordModel);
						response.setMessage("Password updated");
						
						return ResponseEntity.ok(response);
					}else {
						userService.setUserPassword(null, setPasswordModel);
						response.setMessage("Password updated");
						return ResponseEntity.ok(response);
					}
				}
			}
		}catch(Exception e) {
			LOGGER.error("Set password exception", e);
			response.setMessage(String.format("Error setting password for user: %s", e.getMessage()));
			return ResponseEntity.internalServerError().body(response);
		}
	}
	
	
	
}
