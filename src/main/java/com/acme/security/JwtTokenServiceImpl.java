package com.acme.security;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.acme.exception.UserNotFoundException;
import com.acme.jpa.entity.UserEntity;
import com.acme.jpa.repo.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class JwtTokenServiceImpl implements JwtTokenService {

	private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenServiceImpl.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Value("${jwt.secret.key}")
	private String secretKeyString;
	
	@Value("${json.web.token.ttl}")
	private int jwtTokenTtl = -1;
	
	private static final String JWT_COOKIE = "jwtAccessToken";
	
	private static final String UUID_ATTR = "uuid";
	
	private static final String ISSUER = "Acme";
	
	@Override
	public void createCookie(HttpServletResponse response, String subject) throws UserNotFoundException {
		
		Optional<UserEntity> userLookup = userRepository.findByEmailAddress(subject);
		if(userLookup.isPresent()) {
			UserEntity user = userLookup.get();
			
			LOGGER.info("Creating token for user {}", user.getUuid());
			String token = Jwts.builder()
		        .setSubject(subject)
		        .setIssuer(ISSUER)
		        .setIssuedAt(null)
		        .setExpiration(null)
		        .signWith(Keys.hmacShaKeyFor(secretKeyString.getBytes()), SignatureAlgorithm.HS512)
		        .compact();
			
			LOGGER.info("Creating JWT cookie");
			Cookie jwtCookie = new Cookie(JWT_COOKIE, token);
			jwtCookie.setHttpOnly(true);
			jwtCookie.setSecure(true);
			jwtCookie.setMaxAge(jwtTokenTtl);
			jwtCookie.setAttribute(UUID_ATTR, user.getUuid());
			
			response.addCookie(jwtCookie);
			
		}else {
			throw new UserNotFoundException(String.format("Could not create JWT cookie for user %s", subject));
		}
	}
	
	private Optional<Cookie> getCookie(HttpServletRequest request) {
		if(request == null) {
			return Optional.ofNullable(null);
		}else {
			return Arrays.asList(request.getCookies())
				.stream()
				.filter(Objects::nonNull)
				.filter(e -> JWT_COOKIE.equals(e.getName()))
				.findFirst();
		}			
	}
	
	@Override
	public String getUuid(HttpServletRequest request) {
		Optional<Cookie> cookieLookup = getCookie(request);
		if(cookieLookup.isEmpty()) {
			return StringUtils.EMPTY;
		}else {
			return StringUtils.defaultString(cookieLookup.get().getAttribute(UUID_ATTR), StringUtils.EMPTY);
		}
	}
	
	public boolean authorizeRequest(HttpServletRequest request) {
		try {
			
			String token = getToken(request);
			
			if(StringUtils.isNotBlank(token)) {
				Jwts.parserBuilder()
					.setSigningKey(Keys.hmacShaKeyFor(secretKeyString.getBytes()))
					.build()
					.parseClaimsJws(token);
				
				return true;
			}else {
				return false;
			}
		}catch(Exception e) {
			return false;
		}
	}
	
    @Override
    public String getSubject(HttpServletRequest request) {
    	
    	String token = getToken(request);
    	if(StringUtils.isEmpty(token)) {
    		return StringUtils.EMPTY;
    	}
    	
    	Claims jwtClaims = Jwts.parserBuilder()
				.setSigningKey(Keys.hmacShaKeyFor(secretKeyString.getBytes()))
				.build()
				.parseClaimsJws(token)
				.getBody();
    	
    	return jwtClaims.getSubject();
    }
    
	private String getToken(HttpServletRequest request) {
	
		if(request != null && request.getCookies() != null) {
			Optional<Cookie> jwtCookie = Arrays.asList(request.getCookies())
					.stream()
					.filter(Objects::nonNull)
					.filter(cookie -> StringUtils.equals(cookie.getName(), JWT_COOKIE))
					.findFirst();
			
	    	return jwtCookie.isPresent() ? jwtCookie.get().getValue() : StringUtils.EMPTY;
		}else {
			return StringUtils.EMPTY;
		}
	}
	
	@Override
	public void invalidateCookie(HttpServletResponse response) {
		LOGGER.info("Invalidating JWT cookie");
		Cookie jwtCookie = new Cookie(JWT_COOKIE, StringUtils.EMPTY);
		jwtCookie.setMaxAge(0);
		response.addCookie(jwtCookie);
	}
}
