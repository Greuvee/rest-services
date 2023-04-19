package com.acme.jpa.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.acme.jpa.entity.UserValidationEntity;

public interface UserValidationRepository extends JpaRepository<UserValidationEntity, Long>{

	
}
