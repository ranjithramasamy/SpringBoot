package com.jobhub.model;

public class SearchCriteria {
	private String keyword;
	private String skills;
	private String availability;
	private String jobType;
	private String sortby;
	
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
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
	public String getJobType() {
		return jobType;
	}
	public void setJobType(String jobType) {
		this.jobType = jobType;
	}
	public String getSortby() {
		return sortby;
	}
	public void setSortby(String sortby) {
		this.sortby = sortby;
	}
}
