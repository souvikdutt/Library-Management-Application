package com.epam.librarymanager.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.epam.librarymanager.clients.BookClient;
import com.epam.librarymanager.clients.UserClient;
import com.epam.librarymanager.dto.BookDTO;
import com.epam.librarymanager.dto.LibraryDTO;
import com.epam.librarymanager.dto.UserDTO;
import com.epam.librarymanager.error.BookNotFoundException;
import com.epam.librarymanager.error.IssuedMaxNumberOfBooksException;
import com.epam.librarymanager.error.UserNotFoundException;
import com.epam.librarymanager.service.LibraryService;

@RestController
public class LibraryRestController {

	@Autowired
	BookClient bookService;
	@Autowired
	UserClient userService;
	@Autowired
	LibraryService libraryService;

	@GetMapping("/library/books/port")
	public String getPortNumber() {
		return "The port number is "+bookService.getPort();
	}
	
	@GetMapping("/library/books")
	public ResponseEntity<List<BookDTO>> getAllBooks() {
		return bookService.getAllUsers();
	}

	@GetMapping("/library/books/{bookId}")
	public ResponseEntity<BookDTO> getABook(@PathVariable("bookId") int book_id) throws BookNotFoundException {
		return bookService.getBook(book_id);
	}

	@PostMapping("/library/books")
	public ResponseEntity<BookDTO> addNewBook(@RequestBody BookDTO book) {
		return bookService.addBook(book);
	}

	@PutMapping("/library/books/{bookId}")
	public ResponseEntity<BookDTO> updateBook(@PathVariable("bookId") int book_id, @RequestBody BookDTO book)
			throws BookNotFoundException {
		return bookService.updateBook(book_id, book);
	}

	@DeleteMapping("/library/books/{bookId}")
	public void deleteBook(@PathVariable("bookId") int book_id) throws BookNotFoundException {
		libraryService.releaseIssuedBook(book_id);
		bookService.deleteBook(book_id);
	}

	@GetMapping("/library/users")
	public ResponseEntity<List<UserDTO>> getAllUsers() {
		return userService.getAllUsers();
	}

	@GetMapping("/library/users/{username}")
	public ResponseEntity<Map<UserDTO, List<BookDTO>>> viewUserProfile(@PathVariable String username)
			throws UserNotFoundException, BookNotFoundException {
		UserDTO user = userService.getUser(username).getBody();
		Map<UserDTO, List<BookDTO>> mp = new HashMap<>();
		List<BookDTO> books = new ArrayList<>();
		List<Integer> bookIds = libraryService.getBookIdsFromUser(username);
		for (Integer bookId : bookIds) {
			books.add(bookService.getBook(bookId).getBody());
		}
		mp.put(user, books);
		return ResponseEntity.ok(mp);
	}

	@PostMapping("/library/users")
	public ResponseEntity<UserDTO> addUser(@RequestBody UserDTO user) {
		return userService.adduser(user);
	}

	@DeleteMapping("/library/users/{username}")
	public String deleteUserAndHisBooks(@PathVariable String username) throws UserNotFoundException {
		libraryService.deleteUser(username);
		userService.deleteUser(username);
		return username + " is deleted successfully";
	}

	@PutMapping("/library/users/{username}")
	public ResponseEntity<UserDTO> updateUserDetails(@PathVariable String username, @RequestBody UserDTO user) {
		return userService.updateUser(username, user);
	}

	@PostMapping("/library/users/{username}/books/{bookId}")
	public ResponseEntity<LibraryDTO> issueBookToUser(@PathVariable String username, @PathVariable("bookId") int book_id)
			throws UserNotFoundException, BookNotFoundException, IssuedMaxNumberOfBooksException {
		UserDTO user = userService.getUser(username).getBody();
		BookDTO book = bookService.getBook(book_id).getBody();
		return ResponseEntity.ok(libraryService.issueABookToAUser(user.getUsername(), book.getId()));
	}

	@DeleteMapping("/library/users/{username}/books/{bookId}")
	public String releaseBookForUser(@PathVariable String username, @PathVariable("bookId") int book_id) throws BookNotFoundException {
		libraryService.releaseBookForUser(username, book_id);
		return "Book id " + book_id + " released for username " + username + " is successfull";
	}

}
