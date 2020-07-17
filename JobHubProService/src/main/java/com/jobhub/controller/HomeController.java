package com.jobhub.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jobhub.jpa.entity.JobTypes;
import com.jobhub.jpa.entity.Skills;
import com.jobhub.model.SearchCriteria;
import com.jobhub.model.SearchResult;
import com.jobhub.service.HomeService;
import com.jobhub.constants.GlobalConstants;

@CrossOrigin(origins = "http://localhost:3001")
@RestController
@RequestMapping(GlobalConstants.REST_CONTROLLER_ROOT_PATH)
public class HomeController {
	private final HomeService homeService;

	public HomeController(HomeService homeService){
		this.homeService = homeService;
	}

	@GetMapping(GlobalConstants.FETCH_JOB_TYPES_PATH)
	public ResponseEntity<List<JobTypes>> getJobTypes() {
		return new ResponseEntity<List<JobTypes>>(homeService.getAllJobTypes(), HttpStatus.OK);
	}
	
	@GetMapping(GlobalConstants.FETCH_SKILLS_PATH)
	public ResponseEntity<List<Skills>> getSkills() {
		return new ResponseEntity<List<Skills>>(homeService.getAllSkills(), HttpStatus.OK);
	}
	
	@GetMapping(GlobalConstants.JOB_SEARCH_PATH)
	public ResponseEntity<SearchResult> searchJobs(@ModelAttribute SearchCriteria searchCriteria) {
		return new ResponseEntity<SearchResult>(homeService.searchJobs(searchCriteria), HttpStatus.OK);
	}
}