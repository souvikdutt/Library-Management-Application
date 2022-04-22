package com.epam.bookmanager.service;

import java.util.List;

import com.epam.bookmanager.dto.BookDTO;
import com.epam.bookmanager.exception.BookNotFoundException;

public interface BookService {
	
	List<BookDTO> getAllBooks();
	BookDTO getBook(int id) throws BookNotFoundException;
	BookDTO addBook(BookDTO bookDTO);
	void deleteBook(int id) throws BookNotFoundException;
	BookDTO updateBook(int id,BookDTO bookDTO) throws BookNotFoundException;
	
}
