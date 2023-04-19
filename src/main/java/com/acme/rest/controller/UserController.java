package com.acme.rest.controller;

import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.acme.rest.controller.model.UserModel;
import com.acme.rest.controller.model.request.UserPasswordModel;
import com.acme.rest.controller.model.response.UpdatePasswordResponse;
import com.acme.security.JwtTokenService;
import com.acme.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
	
	@Autowired 
	private UserService userService;
	
	@Autowired
	private JwtTokenService jwtTokenService;
	
	@GetMapping
	public ResponseEntity<UserModel> read(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false) String logout){
		
		try {
			if(BooleanUtils.toBoolean(logout)) {
				jwtTokenService.invalidateCookie(response);
				return ResponseEntity.ok().build();
			}else {
				UserModel user = userService.getLoggedInUser(request);
				
				if(user == null) {
					return ResponseEntity.notFound().build();
				}else {
					return ResponseEntity.ok(user);
				}	
			}	
		}catch(Exception e) {
			LOGGER.error("Exception caught during user request", e);
			return ResponseEntity.internalServerError().body(new UserModel(String.format("Error loading logged in user: %s", e.getMessage())));
		}
	}
	
	@PostMapping("/update/password")
	public ResponseEntity<UpdatePasswordResponse> changePassword(HttpServletRequest request, @RequestBody @Valid UserPasswordModel setPasswordModel) {
		
		try {
			userService.setUserPassword(request, setPasswordModel);
			return ResponseEntity.ok(new UpdatePasswordResponse(true, "Password changed"));
		}catch(Exception e) {
			LOGGER.error("Exception caught during update password request", e);
			return ResponseEntity.internalServerError().body(new UpdatePasswordResponse(false, e.getMessage()));
		}
	}
}