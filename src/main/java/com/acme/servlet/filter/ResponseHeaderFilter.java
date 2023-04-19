package com.acme.servlet.filter;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filter to force response headers to disable CORS
 * 
 * @author brethodgson
 *
 */
@Component
public class ResponseHeaderFilter extends OncePerRequestFilter {

	private static final Logger LOGGER = LoggerFactory.getLogger(ResponseHeaderFilter.class);
	
	private static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
	private static final String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";
	private static final String ALL = "*";
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		if(LOGGER.isDebugEnabled()) {
			LOGGER.info("Setting {} to {}", ACCESS_CONTROL_ALLOW_ORIGIN, ALL);
			LOGGER.info("Setting {} to {}", ACCESS_CONTROL_ALLOW_HEADERS, ALL);
		}
		
		response.addHeader(ACCESS_CONTROL_ALLOW_ORIGIN, ALL);
		response.addHeader(ACCESS_CONTROL_ALLOW_HEADERS, ALL);
		
		filterChain.doFilter(request, response);
	}

}