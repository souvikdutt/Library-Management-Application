package com.epam.librarymanager.clients;

import java.util.List;

import javax.validation.Valid;

import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.epam.librarymanager.dto.BookDTO;
import com.epam.librarymanager.error.BookNotFoundException;

@FeignClient(name = "book-service", fallback = BookClientImpl.class)
@LoadBalancerClient(name = "book-service", configuration = BookClientImpl.class)
public interface BookClient {
	
	@GetMapping("/books/port")
	public String getPort();
	
	@GetMapping("/books")
	public ResponseEntity<List<BookDTO>> getAllUsers();
	
	@GetMapping("/books/{book_id}")
	public ResponseEntity<BookDTO> getBook(@Valid @PathVariable("book_id") int id) throws BookNotFoundException;
	
	@PostMapping("/books")
	public ResponseEntity<BookDTO> addBook(@Valid @RequestBody BookDTO book);
	
	@PutMapping("/books/{book_id}") 
	public ResponseEntity<BookDTO> updateBook(@Valid @PathVariable("book_id") int id, @RequestBody BookDTO book) throws BookNotFoundException;
	
	@DeleteMapping("/books/{book_id}")
	public String deleteBook(@Valid @PathVariable("book_id") int id) throws BookNotFoundException;
}
