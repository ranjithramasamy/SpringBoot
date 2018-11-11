package com.jobhub.jpa.repository;

import java.util.List;

import com.jobhub.jpa.entity.Jobs;

public interface SearchJobsCustomRepository {
	public List<Jobs> findAllMatchingJobs();
}