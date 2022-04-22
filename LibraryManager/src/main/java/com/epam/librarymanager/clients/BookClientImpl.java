package com.epam.librarymanager.clients;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.epam.librarymanager.dto.BookDTO;
import com.epam.librarymanager.error.BookNotFoundException;

@Service
public class BookClientImpl implements BookClient {

	@Override
	public String getPort() {
		return "From fallback";
	}

	@Override
	public ResponseEntity<List<BookDTO>> getAllUsers() {
		return ResponseEntity.ok(List.of(new BookDTO(404, "Fallback", "Fallback_publisher", "Fallback_author")));
	}

	@Override
	public ResponseEntity<BookDTO> getBook(@Valid int id) throws BookNotFoundException {
		BookDTO book = new BookDTO(404, "Fallback", "Fallback_publisher", "Fallback_author");
		return ResponseEntity.ok(book);
	}

	@Override
	public ResponseEntity<BookDTO> addBook(@Valid BookDTO book) {
		BookDTO bookDTO = new BookDTO(404, "Fallback", "Fallback_publisher", "Fallback_author");
		return ResponseEntity.ok(bookDTO);
	}

	@Override
	public ResponseEntity<BookDTO> updateBook(@Valid int id, BookDTO book) throws BookNotFoundException {
		BookDTO bookDTO = new BookDTO(404, "Fallback", "Fallback_publisher", "Fallback_author");
		return ResponseEntity.ok(bookDTO);
	}

	@Override
	public String deleteBook(@Valid int id) throws BookNotFoundException {
		return "Deletion from fallback";
	}

}
