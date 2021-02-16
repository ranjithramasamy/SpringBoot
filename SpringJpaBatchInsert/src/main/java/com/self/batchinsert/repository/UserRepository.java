package com.self.batchinsert.repository;

import org.springframework.data.repository.CrudRepository;

import com.self.batchinsert.entity.User;

public interface UserRepository extends CrudRepository<User, Integer> {

}
