package com.jobhub.jpa.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "t_job_loc")
public class JobLocation {
	@Id
	@GeneratedValue
	private Long id;
	
	@OneToOne
    @JoinColumn(name = "eid") 
    private Employer employer;

	private String location;
	
	public JobLocation() {
		super();
	}

	public JobLocation(Long id, Employer employer, String location) {
		super();
		this.id = id;
		this.employer = employer;
		this.location = location;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Employer getEmployer() {
		return employer;
	}

	public void setEmployer(Employer employer) {
		this.employer = employer;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	
}
