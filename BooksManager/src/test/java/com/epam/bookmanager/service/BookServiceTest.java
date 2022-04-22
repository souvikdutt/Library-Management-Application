package com.epam.bookmanager.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.epam.bookmanager.dto.BookDTO;
import com.epam.bookmanager.exception.BookNotFoundException;
import com.epam.bookmanager.model.Book;
import com.epam.bookmanager.repository.BookRepository;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
	
	@InjectMocks
	BookServiceImpl bookService;
	@Mock
	BookRepository bookRepo;
	@Mock
	ModelMapper modelMapper;
	
	private BookDTO bookDTO;
	private Book book;
	private List<BookDTO> bookDtos;
	private List<Book> books;
	
	@BeforeEach
	void setup() {
		this.bookDTO = new BookDTO(1,"Mockito with Souvik","Dutta Publisher","Souvik Dutta");
		this.book = new Book(1,"Mockito with Souvik","Dutta Publisher","Souvik Dutta");
		this.bookDtos = new ArrayList<>();
		this.bookDtos.add(bookDTO);
		this.books = new ArrayList<>();
		this.books.add(book);
	}
	
	@Test
	void getAllBooks() {
		when(bookRepo.findAll()).thenReturn(this.books);
		assertTrue(bookService.getAllBooks().size() > 0);
	}
	
	@Test
	void getBook() throws BookNotFoundException {
		when(bookRepo.findById(Mockito.anyInt())).thenReturn(Optional.of(book));
		when(modelMapper.map(Mockito.any(Book.class), Mockito.any())).thenReturn(this.bookDTO);
		assertEquals(this.bookDTO, bookService.getBook(1));
	}
	
	@Test
	void getBook_invalid() throws BookNotFoundException {
		when(bookRepo.findById(Mockito.anyInt())).thenThrow(BookNotFoundException.class);
		assertThrows(BookNotFoundException.class,()-> bookService.getBook(1));
	}
	
	@Test
	void addBook() {
		when(modelMapper.map(Mockito.any(BookDTO.class), Mockito.any())).thenReturn(this.book);
		when(bookRepo.save(Mockito.any())).thenReturn(book);
		when(modelMapper.map(Mockito.any(Book.class), Mockito.any())).thenReturn(this.bookDTO);
		assertEquals(this.bookDTO, bookService.addBook(bookDTO));
	}
	
	@Test
	void updateBook() {
		when(bookRepo.findById(Mockito.anyInt())).thenReturn(Optional.of(book));
		when(bookRepo.save(Mockito.any())).thenReturn(book);
		when(modelMapper.map(Mockito.any(Book.class), Mockito.any())).thenReturn(this.bookDTO);
		assertEquals(this.bookDTO, bookService.updateBook(1,bookDTO));
	}
	
	@Test
	void updateBook_invalid() {
		when(bookRepo.findById(Mockito.anyInt())).thenThrow(BookNotFoundException.class);
		assertThrows(BookNotFoundException.class,()-> bookService.updateBook(1,bookDTO));
	}
	
	@Test
	void deleteBook() throws BookNotFoundException {
		when(bookRepo.existsById(Mockito.anyInt())).thenReturn(true);
		doNothing().when(bookRepo).deleteById(Mockito.anyInt());
		bookService.deleteBook(1);
	}
	
	@Test
	void deleteBook_invalid() throws BookNotFoundException {
		when(bookRepo.existsById(Mockito.anyInt())).thenReturn(false);
		assertThrows(BookNotFoundException.class,()-> bookService.deleteBook(1));
	}
	
	
	
	
}
