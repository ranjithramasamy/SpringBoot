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

                //If filter criteria presents then keyword will be ignored.
                if (!StringUtils.isEmpty(searchCriteria.getAvailability()) || !StringUtils.isEmpty(searchCriteria.getJobType()) 
                		|| !StringUtils.isEmpty(searchCriteria.getSkills())) {
                	if (!StringUtils.isEmpty(searchCriteria.getAvailability())) {                   
                        predicateList.add(getCompositePredicates(root, cb, "availability", searchCriteria.getAvailability()));
                    }
                    
                    if (!StringUtils.isEmpty(searchCriteria.getJobType())) {
                        final Predicate jobTypePredicate = cb.equal(root.get("jobTypes").get("id"), searchCriteria.getJobType());
                        predicateList.add(jobTypePredicate);
                    }
                    
                    if (!StringUtils.isEmpty(searchCriteria.getSkills())) {                      
                        predicateList.add(getCompositePredicates(root, cb, "skills", searchCriteria.getSkills().toLowerCase()));
                    }                    
                }else {
                	if (!StringUtils.isEmpty(searchCriteria.getKeyword())) {
                        predicateList.add(getPredicatesForKeyword(root, cb, searchCriteria.getKeyword().toLowerCase()));
                    }
                }
                
                Predicate predicate = cb.and(predicateList.toArray(new Predicate[predicateList.size()]));
                
                return predicate;
            }
			
			private Predicate getCompositePredicates(Root<Jobs> root, CriteriaBuilder cb, String searchKey, String searchValue) {
				String[] keys = searchValue.split(",");
        		final List<Predicate> skillsPredicateList = new ArrayList<>();
        		
        		for(String key : keys) {
        			skillsPredicateList.add(cb.like(cb.lower(root.get(searchKey)), "%" + key + "%"));
        		}
        		
                return cb.or(skillsPredicateList.toArray(new Predicate[] {}));             
			}
			
			private Predicate getPredicatesForKeyword(Root<Jobs> root, CriteriaBuilder cb, String searchValue) {
				String[] keys = searchValue.split(",");
        		final List<Predicate> keywordPredicateList = new ArrayList<>();
        		
        		for(String key : keys) {
        			keywordPredicateList.add(cb.like(cb.lower(root.get("skills")), "%" + key + "%"));
        			keywordPredicateList.add(cb.like(cb.lower(root.get("jobTypes").get("jobType")), "%" + key + "%"));
        			keywordPredicateList.add(cb.like(cb.lower(root.get("jobLocation").get("employer").get("name")), "%" + key + "%"));
        			keywordPredicateList.add(cb.like(cb.lower(root.get("jobLocation").get("location")), "%" + key + "%"));
        		}
        		
                return cb.or(keywordPredicateList.toArray(new Predicate[] {}));             
			}
        };
    }


}
