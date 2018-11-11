package com.jobhub.jpa.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import com.jobhub.jpa.entity.Jobs;
import com.jobhub.model.SearchCriteria;

public class JobsSpecification {
	public static Specification<Jobs> findJobsBySpecification(SearchCriteria searchCriteria) {
        return new Specification<Jobs>() {
			private static final long serialVersionUID = 1L;

			@Override
            public Predicate toPredicate(Root<Jobs> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                final List<Predicate> predicateList = new ArrayList<>();
                Predicate predicate = null;
                
                //If filter criteria presents then keyword will be ignored.
                if (!StringUtils.isEmpty(searchCriteria.getAvailability()) || !StringUtils.isEmpty(searchCriteria.getJobType()) 
                		|| !StringUtils.isEmpty(searchCriteria.getSkills())) {
                	if (!StringUtils.isEmpty(searchCriteria.getAvailability())) {
                        final Predicate availabilityPredicate = cb.equal(root.get("availability"), searchCriteria.getAvailability());
                        predicateList.add(availabilityPredicate);
                    }
                    
                    if (!StringUtils.isEmpty(searchCriteria.getJobType())) {
                        final Predicate jobTypePredicate = cb.equal(root.get("jobTypes").get("id"), searchCriteria.getJobType());
                        predicateList.add(jobTypePredicate);
                    }
                    
                    if (!StringUtils.isEmpty(searchCriteria.getSkills())) {
                        final Predicate skillsPredicate = cb.like(cb.lower(root.get("skills")), "%" + searchCriteria.getSkills().toLowerCase() + "%");
                        predicateList.add(skillsPredicate);
                    }
                    
                    predicate = cb.and(predicateList.toArray(new Predicate[predicateList.size()]));
                }else {
                	if (!StringUtils.isEmpty(searchCriteria.getKeyword())) {
                        final Predicate keywordPredicate = cb.like(cb.lower(root.get("skills")), "%" + searchCriteria.getKeyword().toLowerCase() + "%");
                        predicateList.add(keywordPredicate);
                        
                        final Predicate jobTypePredicate = cb.like(cb.lower(root.get("jobTypes").get("jobType")), "%" + searchCriteria.getKeyword().toLowerCase() + "%");
                        predicateList.add(jobTypePredicate);
                        
                        predicate = cb.or(predicateList.toArray(new Predicate[predicateList.size()]));
                    }
                }
                
                return predicate;
            }	
        };
    }


}
