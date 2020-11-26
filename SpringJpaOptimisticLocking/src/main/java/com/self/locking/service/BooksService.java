package com.self.locking.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import com.self.locking.jpa.entity.Books;
import com.self.locking.jpa.repository.BooksRepository;

@Service
@EnableRetry
public class BooksService {
	private static final Logger logger = LoggerFactory.getLogger(BooksService.class);
	
	@Autowired
	BooksRepository booksRepository;

	@Retryable(maxAttempts = 3, backoff = @Backoff(value = 500), value = ObjectOptimisticLockingFailureException.class)
	public void updateBookInfo(Integer bookId, Double price) {
		logger.info("Fetching bookId: " + bookId);
		Optional<Books> book = booksRepository.findById(bookId);

		book.map(entity -> {
			entity.setPrice(price);
			return entity;
		}).ifPresent(entity -> {
			logger.info("Updating the book entity with price: "+ price);
			booksRepository.save(entity);
		});
	}
	
	@Recover
	public void recover(ObjectOptimisticLockingFailureException ex) {
		logger.info(String.format("Failed to update the book price with exception: {0}", ex.getMessage()));
	}
}
