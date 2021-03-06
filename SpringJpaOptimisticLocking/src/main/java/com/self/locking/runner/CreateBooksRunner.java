package com.self.locking.runner;

import com.self.locking.service.BooksService;

public class CreateBooksRunner implements Runnable {

	private final Integer bookId;
	private final Double price;
	private final BooksService booksService;

	public CreateBooksRunner(Integer bookId, Double price, BooksService booksService) {
		this.bookId = bookId;
		this.price = price;
		this.booksService = booksService;
	}

	@Override
	public void run() {
		booksService.updateBookInfo(bookId, price);
	}
}
