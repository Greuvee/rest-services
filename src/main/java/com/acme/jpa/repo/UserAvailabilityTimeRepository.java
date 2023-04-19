package com.acme.jpa.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.acme.jpa.entity.UserAvailabilityTimeEntity;

public interface UserAvailabilityTimeRepository extends JpaRepository<UserAvailabilityTimeEntity, String> {

}
