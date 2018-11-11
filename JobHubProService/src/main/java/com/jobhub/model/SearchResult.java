package com.jobhub.model;

import java.util.List;

public class SearchResult {
	private int total;
	private List<MatchedJob> jobsList;
	
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public List<MatchedJob> getJobsList() {
		return jobsList;
	}
	public void setJobsList(List<MatchedJob> jobsList) {
		this.jobsList = jobsList;
	}
}
