package com.epam.librarymanager.service;

import java.util.List;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epam.librarymanager.dto.LibraryDTO;
import com.epam.librarymanager.error.BookNotFoundException;
import com.epam.librarymanager.error.IssuedMaxNumberOfBooksException;
import com.epam.librarymanager.model.Library;
import com.epam.librarymanager.repository.LibraryRepository;

@Service
public class LibraryServiceImpl implements LibraryService {

	@Autowired
	LibraryRepository libraryRepository;
	@Autowired
	ModelMapper modelMapper;
	
	@Override
	public LibraryDTO issueABookToAUser(String username, int book_id) throws IssuedMaxNumberOfBooksException {

		if (libraryRepository.countIssuedBooksToUser(username) >= 5)
			throw new IssuedMaxNumberOfBooksException();
		
		if(libraryRepository.existsByBookIdAndUsername(book_id, username)) {
			throw new IssuedMaxNumberOfBooksException("Only one copy of book is allowed for a user!");
		}
		
		Library library = Library.builder().username(username).bookId(book_id).build();
		return convertToDto(libraryRepository.save(library));
	}

	@Override
	public List<Integer> getBookIdsFromUser(String username) {
		return libraryRepository.findBookIdByUsername(username);
	}

	@Transactional
	@Override
	public void releaseIssuedBook(int book_id) {
		libraryRepository.deleteAllByBookId(book_id);
	}

	@Transactional
	@Override
	public void releaseBookForUser(String username, int book_id) throws BookNotFoundException {
		if(libraryRepository.existsByBookIdAndUsername(book_id, username))
			libraryRepository.deleteByBookIdAndUsername(book_id, username);
		else
			throw new BookNotFoundException("No book issuing book found for "+ username);
	}

	@Transactional
	@Override
	public void deleteUser(String username) {
		libraryRepository.deleteByUsername(username);
	}

	private LibraryDTO convertToDto(Library library) {
		LibraryDTO libraryDTO = modelMapper.map(library, LibraryDTO.class);
		return libraryDTO;
	}
	
}
