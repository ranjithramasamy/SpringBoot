package com.self.rest.vo;

import java.util.List;

public class Schedules {
	private String date;	
	private List<Topics> topics;
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public List<Topics> getTopics() {
		return topics;
	}
	public void setTopics(List<Topics> topics) {
		this.topics = topics;
	}
}
