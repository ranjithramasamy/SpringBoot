package com.jobhub.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jobhub.jpa.entity.Skills;

public interface SkillsRepository extends JpaRepository<Skills, Long> {
	
}