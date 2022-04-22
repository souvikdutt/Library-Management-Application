package com.epam.librarymanager.service;

import java.util.List;

import com.epam.librarymanager.dto.LibraryDTO;
import com.epam.librarymanager.error.BookNotFoundException;
import com.epam.librarymanager.error.IssuedMaxNumberOfBooksException;

public interface LibraryService {

	LibraryDTO issueABookToAUser(String username,int book_id) throws IssuedMaxNumberOfBooksException;

	List<Integer> getBookIdsFromUser(String username);

	void releaseIssuedBook(int book_id);

	void releaseBookForUser(String username, int book_id) throws BookNotFoundException;

	void deleteUser(String username);
	
}
