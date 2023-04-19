package com.acme.rest.controller;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acme.rest.controller.model.request.AuthRequest;
import com.acme.rest.controller.model.response.AuthResponse;
import com.acme.security.JwtTokenService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/login")
public class LoginController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtTokenService jwtTokenService;
	
	@GetMapping
	public ResponseEntity<HttpStatus> get() {
		return ResponseEntity.ok().build();
	}
	
	@PostMapping
	public ResponseEntity<AuthResponse> login(HttpServletResponse response, @RequestBody AuthRequest authRequest){
		
		if(authRequest == null || StringUtils.isBlank(authRequest.getEmailAddress()) || StringUtils.isBlank(authRequest.getPassword())) {
			return ResponseEntity.badRequest().body(new AuthResponse(false, "Email address and password are required for login"));
		}else {
			try {
				
				Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmailAddress(), authRequest.getPassword()));
				
				if(authentication.isAuthenticated()) {
					
					LOGGER.info("User {} is authenticated", authRequest.getEmailAddress());
					
					User user = (User) authentication.getPrincipal();
					
					jwtTokenService.createCookie(response, user.getUsername());
					
					return ResponseEntity.ok(new AuthResponse(true));	
					
				}else {
					return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse(false, String.format("User %s authentication failed", authRequest.getEmailAddress())));
				}
			} catch(BadCredentialsException e) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse(false, String.format("Bad credentials for user %s", authRequest.getEmailAddress())));
			} catch (Exception e) {
				LOGGER.error(String.format("Login exception for request: %s", authRequest), e);
				return ResponseEntity.internalServerError().body(new AuthResponse(false, e.getMessage()));
			} 
		}
	}
}
