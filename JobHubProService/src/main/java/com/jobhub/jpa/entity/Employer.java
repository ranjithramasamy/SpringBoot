package com.jobhub.jpa.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_employers")
public class Employer {
	@Id
	@GeneratedValue
	private Long id;
	private String name;
	private String address;
	private String rating;
	
	public Employer() {
		super();
	}
	
	public Employer(Long id, String name, String address, String rating) {
		super();
		this.id = id;
		this.name = name;
		this.address = address;
		this.rating = rating;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getRating() {
		return rating;
	}
	public void setRating(String rating) {
		this.rating = rating;
	}
}
