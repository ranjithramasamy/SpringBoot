package com.jobhub.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

@CrossOrigin(origins = "http://localhost:8000", maxAge = 3600)
@RestController
@RequestMapping("/v1/api")
public class HomeController {
	 @Autowired
	 private HomeService homeService;
	
	@GetMapping("/jobtypes")
	public ResponseEntity<List<JobTypes>> getJobTypes() {
		return new ResponseEntity<List<JobTypes>>(homeService.getAllJobTypes(), HttpStatus.OK);
	}
	
	@GetMapping("/skills")
	public ResponseEntity<List<Skills>> getSkills() {
		return new ResponseEntity<List<Skills>>(homeService.getAllSkills(), HttpStatus.OK);
	}
	
	@GetMapping("/search")
	public ResponseEntity<SearchResult> searchJobs(@ModelAttribute SearchCriteria searchCriteria) {
		return new ResponseEntity<SearchResult>(homeService.searchJobs(searchCriteria), HttpStatus.OK);
	}
}