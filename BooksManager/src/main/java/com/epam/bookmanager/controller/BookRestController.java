package com.epam.bookmanager.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.epam.bookmanager.dto.BookDTO;
import com.epam.bookmanager.exception.BookNotFoundException;
import com.epam.bookmanager.service.BookService;

@RestController
public class BookRestController {
	
	@Autowired
	BookService bookService;
	
	@Autowired
	Environment env;
	
	@GetMapping("/books/port")
	public String getPort() {
		return env.getProperty("local.server.port");
	}
	
	@GetMapping("/books")
	public ResponseEntity<List<BookDTO>> getAllBooks() {
		return ResponseEntity.ok(bookService.getAllBooks());
	}
	
	@GetMapping("/books/{book_id}")
	public ResponseEntity<BookDTO> getBook(@Valid @PathVariable("book_id") int id) throws BookNotFoundException {
		return ResponseEntity.ok(bookService.getBook(id));
	}
	
	@PostMapping("/books")
	public ResponseEntity<BookDTO> addBook(@Valid @RequestBody BookDTO bookDTO) {
		return ResponseEntity.ok(bookService.addBook(bookDTO));
	}
 	
	@DeleteMapping("/books/{book_id}")
	public String deleteBook(@Valid @PathVariable("book_id") int id) throws BookNotFoundException {
		bookService.deleteBook(id);
		return id+" deleted successfully";
	}
	
	@PutMapping("/books/{book_id}") 
	public ResponseEntity<BookDTO> updateBook(@Valid @PathVariable("book_id") int id, @RequestBody BookDTO bookDTO) throws BookNotFoundException {
		return ResponseEntity.ok(bookService.updateBook(id, bookDTO));
	}
	
}
