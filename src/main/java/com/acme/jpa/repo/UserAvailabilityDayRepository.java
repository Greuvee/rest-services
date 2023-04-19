package com.acme.jpa.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.acme.jpa.entity.UserAvailabilityDayEntity;

public interface UserAvailabilityDayRepository extends JpaRepository<UserAvailabilityDayEntity, Long> {

}
