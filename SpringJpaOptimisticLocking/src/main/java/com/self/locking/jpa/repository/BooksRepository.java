package com.self.locking.jpa.repository;

import org.springframework.data.repository.CrudRepository;

import com.self.locking.jpa.entity.Books;

public interface BooksRepository extends CrudRepository<Books, Integer> {

}
