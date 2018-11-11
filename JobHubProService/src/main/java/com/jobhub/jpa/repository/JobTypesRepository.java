package com.jobhub.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jobhub.jpa.entity.JobTypes;

public interface JobTypesRepository extends JpaRepository<JobTypes, Long> {
	
}