package com.acme.jpa.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.acme.jpa.entity.MeetingEntity;

public interface MeetingRepository extends JpaRepository<MeetingEntity, String> {
	
}