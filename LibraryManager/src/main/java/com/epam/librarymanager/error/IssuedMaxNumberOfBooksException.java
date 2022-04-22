package com.epam.librarymanager.error;

public class IssuedMaxNumberOfBooksException extends Exception {

	private static final long serialVersionUID = 1L;

	public IssuedMaxNumberOfBooksException() {
		super("User has borrowed max number of books i.e. 5. No more book will be issued...");
	}

	public IssuedMaxNumberOfBooksException(String message) {
		super(message);
	}

	
	
}
