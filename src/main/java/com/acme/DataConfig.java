package com.acme;

import java.util.Arrays;
import java.util.Optional;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.acme.jpa.entity.RoleEntity;
import com.acme.jpa.entity.UserEntity;
import com.acme.jpa.repo.RoleRepository;
import com.acme.jpa.repo.UserRepository;

import jakarta.transaction.Transactional;

@Configuration
public class DataConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(DataConfig.class);
	
	@Bean
    DataSource getDataSource(
    	@Value("${spring.datasource.driver-class-name}") String driverClass,
    	@Value("${spring.datasource.url}") String url,
    	@Value("${spring.datasource.username}") String username,
    	@Value("${spring.datasource.password}") String password ) {
		
        return DataSourceBuilder.create()
          .driverClassName(driverClass)
          .url(url)
          .username(username)
          .password(password)
          .build();	
    }
	
	@Bean
	ApplicationListener<ContextRefreshedEvent> dataLoadListener(BCryptPasswordEncoder passwordEncoder, UserRepository userRepository, RoleRepository roleRepository, @Value("${admin.user.password}") String adminPassword){
		
		return new ApplicationListener<ContextRefreshedEvent>() {
			
			private static final String ADMIN_USER = "ADMIN_USER@localhost.com";
			
			@Override
			public void onApplicationEvent(ContextRefreshedEvent event) {
				initializeRoles();
				createAdminUser();
			}
		
			@Transactional
			private void createAdminUser() {
				
				if(userRepository.findByEmailAddress(ADMIN_USER).isEmpty()) {
				
					LOGGER.info("Administrator user does not exist. Creating user {}", ADMIN_USER);
					
					
					LOGGER.info("Administrator role found - creating {} user", ADMIN_USER);
					
			        UserEntity adminUser = new UserEntity();
			        adminUser.setFirstName("ADMIN");
			        adminUser.setLastName("USER");
			        adminUser.setPassword(passwordEncoder.encode(adminPassword));
			        adminUser.setEmailAddress(ADMIN_USER);
			        adminUser.setPhoneNumber("0000000000");
			        adminUser.setEnabled(true);
			        adminUser.setPasswordExpired(false);
			        
			        userRepository.save(adminUser);
			        
			        Optional<RoleEntity> adminRoleLookup = roleRepository.findByName(SecurityConfig.ROLE_ADMIN);
			        if(adminRoleLookup.isPresent()) {
			        	RoleEntity adminRole = adminRoleLookup.get();
			        	adminRole.addUser(adminUser);
			        	
			        	roleRepository.save(adminRole);
			        	
			        	adminUser.setRoles(Arrays.asList(adminRole));
			        	
			        	userRepository.save(adminUser);
			        }
				}
			}
			
			@Transactional
		    private void initializeRoles() {
		 
				for(String name : SecurityConfig.ROLES) {
					LOGGER.info("Checking for ROLE {}", name);
					Optional<RoleEntity> roleLookup = roleRepository.findByName(name);
					
					if(roleLookup.isEmpty()) {
						LOGGER.info("Role {} was not found - creating", name);
						
						RoleEntity role = new RoleEntity(name);
						roleRepository.save(role);
					}else {
						LOGGER.info("Role {} was found - will not be created", name);
						roleLookup.get().getName();
					}
				}
		    }
		
		};
	}
	
}

