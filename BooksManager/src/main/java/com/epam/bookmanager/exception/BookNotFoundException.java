package com.epam.bookmanager.exception;

public class BookNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public BookNotFoundException() {
		super("Book Not Found");
	}

}
