package com.jobhub.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jobhub.model.JobTypes;
import com.jobhub.repository.MetadataRepository;

@RestController
@RequestMapping("/v1/api/metadata")
public class MetadataController {
	@Autowired
	private MetadataRepository metadataRepository;
	
	@GetMapping("/jobtypes")
	public ResponseEntity<List<JobTypes>> getJobTypes() {
		return new ResponseEntity<List<JobTypes>>(metadataRepository.findAll(), HttpStatus.OK);
	}
}