package com.acme.rest.controller;

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

import com.acme.SecurityConfig;
import com.acme.exception.RoleNotFoundException;
import com.acme.rest.controller.model.UserModel;
import com.acme.rest.controller.model.UserRoleModel;
import com.acme.service.AdminService;
import com.acme.service.UserService;

import io.micrometer.common.util.StringUtils;

@RestController
@RequestMapping("/admin")
public class AdminController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AdminService adminService;
	
	private static final String ACTION_ASSIGN = "assign";
	
	@PostMapping("/users/role")
	public ResponseEntity<UserModel> assignUserRole(@RequestBody UserRoleModel userRole) throws RoleNotFoundException {
		
		if(userRole == null) {
			return ResponseEntity.badRequest().body(new UserModel("Request object is required to assign role"));
		}else if(StringUtils.isBlank(userRole.getEmailAddress())) {
			return ResponseEntity.badRequest().body(new UserModel("Email address is required to assign role"));
		}else if(StringUtils.isBlank(userRole.getRole())) {
			return ResponseEntity.badRequest().body(new UserModel("Role is required to assign role"));
		}else if(StringUtils.isBlank(userRole.getAction())) {
			return ResponseEntity.badRequest().body(new UserModel("Action is required to assign role"));
		}else {
			try {
				
				if(SecurityConfig.ROLE_ADVOCATE.equals(userRole.getRole())) {
					if(ACTION_ASSIGN.equals(userRole.getAction())) {
						LOGGER.info("Assigning role {} to user {}", userRole.getRole(), userRole.getEmailAddress());
						adminService.assignAdvocateRoleToUser(userRole.getEmailAddress());
						
						return ResponseEntity.ok(userService.getUserByEmailAddress(userRole.getEmailAddress()));
					}else {
						LOGGER.warn("Unknown action {} for role {} requested for user {}", userRole.getAction(), userRole.getRole(), userRole.getEmailAddress());
						return ResponseEntity.badRequest().body(new UserModel(String.format("Updating user %s with role %s or action %s is not valid", userRole.getEmailAddress(), userRole.getRole(), userRole.getAction())));
					}
				}else {
					LOGGER.warn("Unknown role {} requested for user {}", userRole.getRole(), userRole.getEmailAddress());
					return ResponseEntity.badRequest().body(new UserModel(String.format("Unknown role %s requested for user %s", userRole.getRole(), userRole.getEmailAddress())));
				}
			}catch(Exception e) {
				LOGGER.error("Exception updating user role", e);
				return ResponseEntity.internalServerError().body(new UserModel(String.format("Error updating user role: %s", e.getMessage())));
			}
		}
	}
	
	@GetMapping("/users")
	public ResponseEntity<UserModel> getUserDetails(@RequestParam("emailAddress") String emailAddress){
		
		try {
			UserModel userDetails = userService.getUserByEmailAddress(emailAddress);
			
			if(userDetails != null) {
				return ResponseEntity.ok(userDetails);
			}else {
				return ResponseEntity.notFound().build();
			}
		}catch(Exception e) {
			return ResponseEntity.internalServerError().body(new UserModel(e.getMessage()));
		}
	}
	
}
