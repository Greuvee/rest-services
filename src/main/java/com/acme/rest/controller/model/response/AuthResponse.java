package com.acme.rest.controller.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthResponse {
	
	private boolean authenticated;
	
	private String message;
	
	private AuthResponse() {
		super();
	}
	
	public AuthResponse(boolean authenticated) {
		this();
		setAuthenticated(authenticated);
	}
	
	public AuthResponse(boolean authenticated, String message) {
		this(authenticated);
		setMessage(message);
	}

	public boolean isAuthenticated() {
		return authenticated;
	}

	public void setAuthenticated(boolean authenticated) {
		this.authenticated = authenticated;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
