package com.jobhub.jpa.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "t_jobs")
@NamedQuery(name = "fetchMatchingJobs", query = "select j.id, j.skills, j.availability, j.postedOn, j.jobDesc, j.tags from Jobs j")
public class Jobs {
	@Id
	@GeneratedValue
	private Long id;
	
	@OneToOne
    @JoinColumn(name = "loc_id") 
    private JobLocation jobLocation;
	
	@OneToOne 
    @JoinColumn(name = "job_type_id") 
    private JobTypes jobTypes;
	
	private String skills;
	private String availability;
	
	@Column(name="posted_on")
	private Date postedOn;
	
	@Column(name="wage_per_hr")
	private String wagePerHour;
	
	@Column(name="job_desc")
	private String jobDesc;
	
	private String tags;
	
	public Jobs() {
		super();
	}

	public Jobs(Long id, JobLocation jobLocation, JobTypes jobTypes, String skills, String availability, Date postedOn,
			String wagePerHour, String jobDesc, String tags) {
		super();
		this.id = id;
		this.jobLocation = jobLocation;
		this.jobTypes = jobTypes;
		this.skills = skills;
		this.availability = availability;
		this.postedOn = postedOn;
		this.wagePerHour = wagePerHour;
		this.jobDesc = jobDesc;
		this.tags = tags;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public JobLocation getJobLocation() {
		return jobLocation;
	}

	public void setJobLocation(JobLocation jobLocation) {
		this.jobLocation = jobLocation;
	}

	public JobTypes getJobTypes() {
		return jobTypes;
	}

	public void setJobTypes(JobTypes jobTypes) {
		this.jobTypes = jobTypes;
	}

	public String getSkills() {
		return skills;
	}

	public void setSkills(String skills) {
		this.skills = skills;
	}

	public String getAvailability() {
		return availability;
	}

	public void setAvailability(String availability) {
		this.availability = availability;
	}

	public Date getPostedOn() {
		return postedOn;
	}

	public void setPostedOn(Date postedOn) {
		this.postedOn = postedOn;
	}

	public String getWagePerHour() {
		return wagePerHour;
	}

	public void setWagePerHour(String wagePerHour) {
		this.wagePerHour = wagePerHour;
	}

	public String getJobDesc() {
		return jobDesc;
	}

	public void setJobDesc(String jobDesc) {
		this.jobDesc = jobDesc;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}
}
