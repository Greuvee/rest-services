package com.acme.service;

import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.acme.SecurityConfig;
import com.acme.exception.RoleNotFoundException;
import com.acme.jpa.entity.RoleEntity;
import com.acme.jpa.entity.UserEntity;
import com.acme.jpa.repo.RoleRepository;
import com.acme.jpa.repo.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class AdminServiceImpl implements AdminService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AdminServiceImpl.class);

	@Autowired
	private AdvocateService advocateService;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Transactional
	@Override
	public void assignAdvocateRoleToUser(String emailAddress) throws RoleNotFoundException {
		
		LOGGER.info("Attempting to assign {} to user {}", SecurityConfig.ROLE_ADVOCATE, emailAddress);
		
		Optional<UserEntity> userLookup = userRepository.findByEmailAddress(emailAddress);
		
		if(userLookup.isPresent()) {
			LOGGER.info("User {} was found - Preparing for new role updates", emailAddress);
			
			UserEntity user = userLookup.get();
			
			Optional<RoleEntity> roleLookup = user.getRoles().stream().filter(Objects::nonNull).filter(role -> StringUtils.equals(role.getName(), SecurityConfig.ROLE_ADVOCATE)).findAny();
			
			if(roleLookup.isPresent()) {
				LOGGER.info("User {} is already assigned the {} role", emailAddress, SecurityConfig.ROLE_ADVOCATE);
			}else {
				LOGGER.info("Assigning role {} to user {}", SecurityConfig.ROLE_ADVOCATE, emailAddress);
				
				roleLookup = roleRepository.findByName(SecurityConfig.ROLE_ADVOCATE);
				if(roleLookup.isEmpty()) {
					throw new RoleNotFoundException(String.format("Could not locate role %s for assignment to user %s", SecurityConfig.ROLE_ADVOCATE, emailAddress));
				}else {
					
					RoleEntity advocateRole = roleLookup.get();
					
					advocateRole.addUser(user);
					roleRepository.save(advocateRole);
					
					user.addRole(advocateRole);
					
					
					LOGGER.info("Successfully added role {} to user {}", SecurityConfig.ROLE_ADVOCATE, emailAddress);
					
					advocateService.initializeDefaultAvailability(user.getUuid());
				}	
			}
		}else {
			LOGGER.info("User for email address {} was not found", emailAddress);
		}
	}
}