package com.jobhub.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.jobhub.constants.JobHubServiceConstants;
import com.jobhub.jpa.entity.JobTypes;
import com.jobhub.jpa.entity.Jobs;
import com.jobhub.jpa.entity.Skills;
import com.jobhub.jpa.repository.JobTypesRepository;
import com.jobhub.jpa.repository.JobsSpecification;
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
		return jobTypesRepository.findAll(new Sort(Sort.Direction.ASC, "jobType"));
	}

	@Override
	public List<Skills> getAllSkills() {
		return skillsRepository.findAll(new Sort(Sort.Direction.ASC, "desc"));
	}

	@Override
	public SearchResult searchJobs(SearchCriteria searchCriteria) {
		SearchResult searchResult = new SearchResult();
		String sortField = searchCriteria.getSortby();
		
		if(StringUtils.isEmpty(sortField)) {
			sortField = JobHubServiceConstants.SORT_BY_POSTED_DT;
		}
		
		List<Jobs> matchedJobsList = searchJobsRepository.findAll(JobsSpecification.findJobsBySpecification(searchCriteria), new Sort(Sort.Direction.DESC, sortField));
		List<MatchedJob> formattedJobsList = new ArrayList<>();
		
		for(Jobs job: matchedJobsList) {
			String[] skills = job.getSkills().split("\\|", -1);
			String availability = "hourly";
			
			if(Integer.parseInt(job.getAvailability()) > 1) {
				availability = "part-time";
			}
			
			MatchedJob matchedJob = new MatchedJob(job.getId(), job.getJobLocation().getLocation(), 
					job.getJobLocation().getEmployer().getName(), job.getJobTypes().getJobType(), skills, availability, 
					job.getPostedOn(), job.getWagePerHour(), job.getJobDesc(), job.getJobLocation().getEmployer().getRating());
			
			formattedJobsList.add(matchedJob);
		}
		
		searchResult.setTotal(matchedJobsList.size());
		searchResult.setJobsList(formattedJobsList);
		
		return searchResult;
	}
}
