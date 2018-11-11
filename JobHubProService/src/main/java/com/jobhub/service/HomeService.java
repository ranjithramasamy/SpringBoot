package com.jobhub.service;

import java.util.List;

import com.jobhub.jpa.entity.JobTypes;
import com.jobhub.jpa.entity.Skills;
import com.jobhub.model.SearchCriteria;
import com.jobhub.model.SearchResult;

public interface HomeService {
	List<JobTypes> getAllJobTypes();
	
	List<Skills> getAllSkills();
	
	SearchResult searchJobs(SearchCriteria searchCriteria);
}
