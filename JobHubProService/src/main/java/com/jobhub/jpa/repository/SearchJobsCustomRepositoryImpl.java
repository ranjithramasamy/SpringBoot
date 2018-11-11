package com.jobhub.jpa.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.jobhub.jpa.entity.Jobs;

@Repository
@Transactional(readOnly = true)
public class SearchJobsCustomRepositoryImpl implements SearchJobsCustomRepository {
	@PersistenceContext
    EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Jobs> findAllMatchingJobs() {
		Query query = entityManager.createQuery("select j from Jobs as j", Jobs.class);

        return query.getResultList();
	}

}
