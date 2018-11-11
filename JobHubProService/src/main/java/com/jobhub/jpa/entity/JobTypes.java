package com.jobhub.jpa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_job_types")
public class JobTypes {
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(name="job_type")
	private String jobType;
	
	public JobTypes() {
		super();
	}
	
	public JobTypes(Long id, String jobType) {
		super();
		this.id = id;
		this.jobType = jobType;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getJobType() {
		return jobType;
	}
	public void setJobType(String type) {
		this.jobType = type;
	}
}
