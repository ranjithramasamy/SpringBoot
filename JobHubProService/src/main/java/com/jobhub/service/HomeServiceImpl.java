package com.jobhub.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.jobhub.jpa.entity.JobTypes;
import com.jobhub.jpa.entity.Jobs;
import com.jobhub.jpa.entity.Skills;
import com.jobhub.jpa.repository.JobTypesRepository;
import com.jobhub.jpa.repository.SearchJobsRepository;
import com.jobhub.jpa.repository.SkillsRepository;
import com.jobhub.model.MatchedJob;
import com.jobhub.model.SearchCriteria;
import com.jobhub.model.SearchResult;

@Service
public class HomeServiceImpl implements HomeService {
	@Autowired
	private JobTypesRepository jobTypesRepository;
	
	@Autowired
	private SkillsRepository skillsRepository;
	
	@Autowired
	private SearchJobsRepository searchJobsRepository;
	
	@Override
	public List<JobTypes> getAllJobTypes() {		
		return jobTypesRepository.findAll(sortByAsc("jobType"));
	}

	@Override
	public List<Skills> getAllSkills() {
		return skillsRepository.findAll(sortByAsc("desc"));
	}

	@Override
	public SearchResult searchJobs(SearchCriteria searchCriteria) {
		SearchResult searchResult = new SearchResult();
		List<Jobs> matchedJobsList = searchJobsRepository.findAll();
		List<MatchedJob> formattedJobsList = new ArrayList<>();
		
		for(Jobs job: matchedJobsList) {
			MatchedJob matchedJob = new MatchedJob(job.getId(), job.getJobLocation().getLocation(), 
					job.getJobLocation().getEmployer().getName(), job.getJobTypes().getJobType(), job.getSkills(), job.getAvailability(), 
					job.getPostedOn(), job.getWagePerHour(), job.getJobDesc(), job.getTags(), job.getJobLocation().getEmployer().getRating());
			
			formattedJobsList.add(matchedJob);
		}
		
		searchResult.setTotal(matchedJobsList.size());
		searchResult.setJobsList(formattedJobsList);
		
		return searchResult;
	}
	
	private Sort sortByAsc(String key) {
        return new Sort(Sort.Direction.ASC, key);
    }
}
