package com.jobhub.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.jobhub.jpa.entity.Jobs;

public interface SearchJobsRepository extends JpaRepository<Jobs, Long>, JpaSpecificationExecutor<Jobs> {

}