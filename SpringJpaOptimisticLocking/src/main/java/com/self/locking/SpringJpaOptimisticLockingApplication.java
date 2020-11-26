package com.self.locking;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.self.locking.jpa.entity.Books;
import com.self.locking.jpa.repository.BooksRepository;
import com.self.locking.runner.CreateBooksRunner;
import com.self.locking.service.BooksService;

@SpringBootApplication
public class SpringJpaOptimisticLockingApplication {
	private static final Logger logger = LoggerFactory.getLogger(SpringJpaOptimisticLockingApplication.class);
	private ExecutorService es = Executors.newFixedThreadPool(5);
	
	@Autowired
	BooksService booksService;
	
	@Autowired
	BooksRepository booksRepository;

	@PostConstruct
	public void intialize() {		
		List<Double> priceToUpdateList = Arrays.asList(234.5d, 456.34d, 345.2d, 90.02d, 34.56d, 754.32d, 95.2d, 122.34d, 133.45d, 345.11d, 490.22d, 495.01d, 921.31d);

		Books savedBooksEntity = booksRepository.save(new Books("Java 8", "David Frank", 450.0d));

		logger.info("Create workers and add it into executor service for concurrent processing.");
		priceToUpdateList.stream().forEach(price -> {
			Runnable worker = new CreateBooksRunner(savedBooksEntity.getId(), price, booksService);
			es.execute(worker);
		});
	}
	
	@PreDestroy
	public void cleanup() {
		if (es != null) {
			logger.info("Shutting down the executor service.");
			es.shutdown();
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringJpaOptimisticLockingApplication.class, args);
	}

}
