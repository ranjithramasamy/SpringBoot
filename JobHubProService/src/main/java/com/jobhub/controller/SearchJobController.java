package com.jobhub.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api")
public class SearchJobController {
	@GetMapping("/search")
	public ResponseEntity<String> searchJobs(@RequestParam("kw") String keyWord, 
			@RequestParam("fs") String skills,
			@RequestParam("fa") String availability,
			@RequestParam("fjt") String jobType,
			@RequestParam("sb") String sortBy) {
		return new ResponseEntity<String>("keyWord: " + keyWord + " skills: " + skills + " availability: " + availability + " jobType: " + jobType + " sortBy: " + sortBy, HttpStatus.OK);
	}
}
