package com.epam.bookmanager.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epam.bookmanager.dto.BookDTO;
import com.epam.bookmanager.exception.BookNotFoundException;
import com.epam.bookmanager.model.Book;
import com.epam.bookmanager.repository.BookRepository;

@Service
public class BookServiceImpl implements BookService {

	@Autowired
	BookRepository bookRepository;
	@Autowired
	ModelMapper modelMapper;
	
	@Override
	public List<BookDTO> getAllBooks() {
		return bookRepository.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
	}
	
	@Override
	public BookDTO getBook(int id) throws BookNotFoundException {
		Book book = bookRepository.findById(id).orElseThrow(()->new BookNotFoundException());
		return convertToDto(book);
	}

	@Override
	public BookDTO addBook(BookDTO bookDTO) {
		Book book = bookRepository.save(convertToEntity(bookDTO));
		return convertToDto(book);
	}

	@Override
	public void deleteBook(int id) throws BookNotFoundException {
		if(bookRepository.existsById(id))
			bookRepository.deleteById(id);
		else
			throw new BookNotFoundException();
	}

	@Override
	public BookDTO updateBook(int id, BookDTO bookDTO) throws BookNotFoundException {
		Book existedBook = bookRepository.findById(id).orElseThrow(()->new BookNotFoundException());
		
		if(Objects.nonNull(bookDTO.getName()) && !"".equalsIgnoreCase(bookDTO.getName())) {
			existedBook.setName(bookDTO.getName());
		}
		if(Objects.nonNull(bookDTO.getAuthor()) && !"".equalsIgnoreCase(bookDTO.getAuthor())) {
			existedBook.setAuthor(bookDTO.getAuthor());
		}
		if(Objects.nonNull(bookDTO.getPublisher()) && !"".equalsIgnoreCase(bookDTO.getPublisher())) {
			existedBook.setPublisher(bookDTO.getPublisher());
		}
		
		return convertToDto(bookRepository.save(existedBook));
	}
	
	private BookDTO convertToDto(Book book) {
		BookDTO bookDTO = modelMapper.map(book, BookDTO.class);
		return bookDTO;
	}
	
	private Book convertToEntity(BookDTO bookDTO) {
		Book book = modelMapper.map(bookDTO, Book.class);
		return book;
	}

}
