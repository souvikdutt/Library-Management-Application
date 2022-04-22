package com.epam.librarymanager.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.epam.librarymanager.dto.LibraryDTO;
import com.epam.librarymanager.error.BookNotFoundException;
import com.epam.librarymanager.error.IssuedMaxNumberOfBooksException;
import com.epam.librarymanager.model.Library;
import com.epam.librarymanager.repository.LibraryRepository;

@ExtendWith(MockitoExtension.class)
public class LibraryServiceTest {

	@InjectMocks
	LibraryServiceImpl libraryService;
	@Mock
	LibraryRepository libraryRepo;
	@Mock
	ModelMapper modelMapper;
	
	
	private LibraryDTO libraryDTO;
	private Library library;
	
	@BeforeEach
	void setup() {
		this.libraryDTO = new LibraryDTO(1, "souvik001", 11);
		this.library = new Library(1, "souvik001", 11);
	}
	
	@Test
	void issueABookToAUser() throws IssuedMaxNumberOfBooksException {
		when(libraryRepo.countIssuedBooksToUser(Mockito.anyString())).thenReturn(1);
		when(libraryRepo.existsByBookIdAndUsername(Mockito.anyInt(), Mockito.anyString())).thenReturn(false);
		when(libraryRepo.save(Mockito.any())).thenReturn(library);
		when(modelMapper.map(Mockito.any(Library.class), Mockito.any())).thenReturn(libraryDTO);
		assertEquals(this.libraryDTO, libraryService.issueABookToAUser("souvik001", 1));
	}
	
	@Test
	void issueABookToAUser_invalid1() throws IssuedMaxNumberOfBooksException {
		when(libraryRepo.countIssuedBooksToUser(Mockito.anyString())).thenReturn(6);
		assertThrows(IssuedMaxNumberOfBooksException.class, ()->libraryService.issueABookToAUser("souvik001", 1));
	}
	
	@Test
	void issueABookToAUser_invalid2() throws IssuedMaxNumberOfBooksException {
		when(libraryRepo.countIssuedBooksToUser(Mockito.anyString())).thenReturn(1);
		when(libraryRepo.existsByBookIdAndUsername(Mockito.anyInt(), Mockito.anyString())).thenReturn(true);
		assertThrows(IssuedMaxNumberOfBooksException.class, ()->libraryService.issueABookToAUser("souvik001", 1));
	}
	
	@Test
	void getBookIdsFromUser() {
		when(libraryRepo.findBookIdByUsername(Mockito.anyString())).thenReturn(List.of(1));
		assertTrue(libraryService.getBookIdsFromUser("souvik001").size()>0);
		verify(libraryRepo, atLeastOnce()).findBookIdByUsername(Mockito.anyString());
	}
	
	@Test
	void releaseIssuedBook() {
		doNothing().when(libraryRepo).deleteAllByBookId(Mockito.anyInt());
		libraryService.releaseIssuedBook(1);
		verify(libraryRepo, times(1)).deleteAllByBookId(Mockito.anyInt());
	}
	
	@Test
	void releaseBookForUser() throws BookNotFoundException {
		when(libraryRepo.existsByBookIdAndUsername(Mockito.anyInt(), Mockito.anyString())).thenReturn(true);
		doNothing().when(libraryRepo).deleteByBookIdAndUsername(Mockito.anyInt(), Mockito.anyString());
		libraryService.releaseBookForUser("souvik001", 1);
	}
	
	@Test
	void releaseBookForUser_invalid() throws BookNotFoundException {
		when(libraryRepo.existsByBookIdAndUsername(Mockito.anyInt(), Mockito.anyString())).thenReturn(false);
		assertThrows(BookNotFoundException.class, ()->libraryService.releaseBookForUser("souvik001", 1));
	}
	
	@Test
	void deleteUser() {
		doNothing().when(libraryRepo).deleteByUsername(Mockito.anyString());
		libraryService.deleteUser("souvik001");
		verify(libraryRepo, times(1)).deleteByUsername(Mockito.anyString());
	}
	
}
