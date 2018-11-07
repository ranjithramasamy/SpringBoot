package com.jobhub.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jobhub.model.JobTypes;

public interface MetadataRepository extends JpaRepository<JobTypes, Long> {

}