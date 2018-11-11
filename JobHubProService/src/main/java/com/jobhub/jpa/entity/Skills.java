package com.jobhub.jpa.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_skills")
public class Skills {
	@Id
	@GeneratedValue
	private Long id;
	private String desc;
	
	public Skills() {
		super();
	}
	
	public Skills(Long id, String desc) {
		super();
		this.id = id;
		this.desc = desc;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
