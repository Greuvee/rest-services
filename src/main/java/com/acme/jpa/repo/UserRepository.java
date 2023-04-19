package com.acme.jpa.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.acme.jpa.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, String>{

	Optional<UserEntity> findByEmailAddress(String emailAddress);
	
	Optional<UserEntity> findByPhoneNumber(String phoneNumber);
}
