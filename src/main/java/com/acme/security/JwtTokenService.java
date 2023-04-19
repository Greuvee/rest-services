package com.acme.security;

import com.acme.exception.UserNotFoundException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface JwtTokenService {

	String getUuid(HttpServletRequest request);
	
	String getSubject(HttpServletRequest requet);
	
	void createCookie(HttpServletResponse response, String accessToken) throws UserNotFoundException;
	
	void invalidateCookie(HttpServletResponse response);
	
	boolean authorizeRequest(HttpServletRequest request);
}
