package com.acme;

import java.util.Set;

import org.apache.commons.collections4.SetUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder.BCryptVersion;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.acme.service.UserService;
import com.acme.servlet.filter.JwtTokenFilter;
import com.acme.servlet.filter.ResponseHeaderFilter;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class SecurityConfig {
	
	public static final String ROLE_USER = "ROLE_USER";
	public static final String ROLE_ADVOCATE = "ROLE_ADVOCATE";
	public static final String ROLE_ADMIN = "ROLE_ADMIN";
	
	public static final Set<String> ROLES = SetUtils.unmodifiableSet(ROLE_USER, ROLE_ADVOCATE, ROLE_ADMIN);
	
	@Bean
	BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(BCryptVersion.$2A, 10);
	}
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http, JwtTokenFilter jwtTokenFilter, ResponseHeaderFilter responseHeaderFilter) throws Exception {
		
		return http
			.csrf().disable()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
            .authorizeHttpRequests()
            .requestMatchers("/register/**", "/login", "/logout", "/webhook/**").permitAll()
            .requestMatchers("/users/**", "/meetings/**").hasAnyAuthority(ROLE_USER, ROLE_ADMIN)
            .requestMatchers("/advocates/**", "/meetings/**").hasAnyAuthority(ROLE_ADVOCATE, ROLE_ADMIN)
            .requestMatchers("/admin/**").hasAnyAuthority(ROLE_ADMIN)
            .anyRequest().authenticated()
            .and()
            .exceptionHandling()
            .authenticationEntryPoint((request, response, ex) -> 
                response.sendError(
                        HttpServletResponse.SC_UNAUTHORIZED,
                        ex.getMessage()
                    )
                )
            .and()
            .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterAfter(responseHeaderFilter, AuthorizationFilter.class)
            .build();
	}
	
	@Bean
	AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder, UserService userService) 
	  throws Exception {
	    return http.getSharedObject(AuthenticationManagerBuilder.class)
	      .userDetailsService(userService)
	      .passwordEncoder(bCryptPasswordEncoder)
	      .and()
	      .build();
	}
}
