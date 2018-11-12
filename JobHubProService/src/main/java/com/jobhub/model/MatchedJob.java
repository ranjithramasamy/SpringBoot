package com.jobhub.model;

import java.util.Date;

public class MatchedJob {
	private Long jobId;
    private String jobLocation;
    private String employer;
    private String jobType;
	private String[] skills;
	private String availability;
	private Date postedOn;
	private String wagePerHour;
	private String jobDesc;
	private String rating;
	
	public MatchedJob() {
		super();
	}

	public MatchedJob(Long jobId, String jobLocation, String employer, String jobType, String[] skills, String availability,
			Date postedOn, String wagePerHour, String jobDesc, String rating) {
		super();
		this.jobId = jobId;
		this.jobLocation = jobLocation;
		this.employer = employer;
		this.jobType = jobType;
		this.skills = skills;
		this.availability = availability;
		this.postedOn = postedOn;
		this.wagePerHour = wagePerHour;
		this.jobDesc = jobDesc;
		this.rating = rating;
	}

	public Long getJobId() {
		return jobId;
	}

	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}

	public String getJobLocation() {
		return jobLocation;
	}

	public void setJobLocation(String jobLocation) {
		this.jobLocation = jobLocation;
	}

	public String getEmployer() {
		return employer;
	}

	public void setEmployer(String employer) {
		this.employer = employer;
	}

	public String getJobType() {
		return jobType;
	}

	public void setJobType(String jobType) {
		this.jobType = jobType;
	}

	public String[] getSkills() {
		return skills;
	}

	public void setSkills(String[] skills) {
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
	
	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}
}
