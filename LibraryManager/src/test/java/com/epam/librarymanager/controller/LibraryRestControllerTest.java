package com.epam.librarymanager.controller;


import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.epam.librarymanager.clients.BookClient;
import com.epam.librarymanager.clients.UserClient;
import com.epam.librarymanager.dto.BookDTO;
import com.epam.librarymanager.dto.LibraryDTO;
import com.epam.librarymanager.dto.UserDTO;
import com.epam.librarymanager.error.BookNotFoundException;
import com.epam.librarymanager.error.IssuedMaxNumberOfBooksException;
import com.epam.librarymanager.error.UserNotFoundException;
import com.epam.librarymanager.service.LibraryService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest
public class LibraryRestControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	LibraryService libraryService;
	@MockBean
	BookClient bookClient;
	@MockBean
	UserClient userClient;
	
	private LibraryDTO libraryDTO;
	private BookDTO bookDTO;
	private List<BookDTO> bookDtos;
	private UserDTO user;
	private List<UserDTO> users;
	private Map<UserDTO, List<BookDTO>> mpp;
	private ObjectMapper mapper;
	
	@BeforeEach
	void setup() {
		this.mapper = new ObjectMapper();
		this.libraryDTO = new LibraryDTO(1, "souvik001", 11);
		this.bookDTO = new BookDTO(1,"Mockito with Souvik","Dutta Publisher","Souvik Dutta");
		this.bookDtos = new ArrayList<>();
		this.bookDtos.add(bookDTO);
		this.user = new UserDTO("user1", "souvik@gmail.com", "Souvik Dutta");
		this.users = new ArrayList<>();
		this.users.add(user);
		this.mpp = new HashMap<>();
		this.mpp.put(user, bookDtos);
	}
	
	@Test
	void getAllBooks() {
		assertDoesNotThrow(()->{
			when(bookClient.getAllUsers()).thenReturn(ResponseEntity.ok(bookDtos));
			
			RequestBuilder reqBuilder = MockMvcRequestBuilders
					.get("/library/books")
					.content(mapper.writeValueAsString(this.bookDtos))
					.contentType(MediaType.APPLICATION_JSON);
			mockMvc.perform(reqBuilder)
			.andExpect(jsonPath("$[0].id", is(1)))
			.andExpect(jsonPath("$[0].name", is("Mockito with Souvik")))
			.andExpect(jsonPath("$[0].publisher", is("Dutta Publisher")))
			.andExpect(jsonPath("$[0].author", is("Souvik Dutta")));
		});
	}
	
	@Test
	void getABook() {
		assertDoesNotThrow(()-> {
			when(bookClient.getBook(Mockito.anyInt())).thenReturn(ResponseEntity.ok(this.bookDTO));
			RequestBuilder reqBuilder = MockMvcRequestBuilders
					.get("/library/books/1")
					.content(mapper.writeValueAsString(this.bookDTO))
					.contentType(MediaType.APPLICATION_JSON);
			mockMvc.perform(reqBuilder)
			.andExpect(jsonPath("$.id", is(1)))
			.andExpect(jsonPath("$.name", is("Mockito with Souvik")))
			.andExpect(jsonPath("$.publisher", is("Dutta Publisher")))
			.andExpect(jsonPath("$.author", is("Souvik Dutta")));
			}
		);
	}
	
	@Test
	void getBook_invalid() throws Exception {
		when(bookClient.getBook(Mockito.anyInt())).thenThrow(BookNotFoundException.class);
		RequestBuilder reqBuilder = MockMvcRequestBuilders
				.get("/library/books/2")
				.content(mapper.writeValueAsString(this.bookDTO))
				.contentType(MediaType.APPLICATION_JSON);
		mockMvc.perform(reqBuilder)
		.andExpect(status().isNotFound());
	}
	
	@Test
	void addBook() {
		assertDoesNotThrow(()-> {
			when(bookClient.addBook(Mockito.any(BookDTO.class))).thenReturn(ResponseEntity.ok(this.bookDTO));
			RequestBuilder reqBuilder = MockMvcRequestBuilders
					.post("/library/books")
					.content(mapper.writeValueAsString(this.bookDTO))
					.contentType(MediaType.APPLICATION_JSON);
			mockMvc.perform(reqBuilder)
			.andExpect(jsonPath("$.id", is(1)))
			.andExpect(jsonPath("$.name", is("Mockito with Souvik")))
			.andExpect(jsonPath("$.publisher", is("Dutta Publisher")))
			.andExpect(jsonPath("$.author", is("Souvik Dutta")));
			}
		);
	}
	
	@Test
	void updateBook() {
		assertDoesNotThrow(()-> {
			when(bookClient.updateBook(Mockito.anyInt(),Mockito.any(BookDTO.class))).thenReturn(ResponseEntity.ok(this.bookDTO));
			RequestBuilder reqBuilder = MockMvcRequestBuilders
					.put("/library/books/1")
					.content(mapper.writeValueAsString(this.bookDTO))
					.contentType(MediaType.APPLICATION_JSON);
			mockMvc.perform(reqBuilder)
			.andExpect(jsonPath("$.id", is(1)))
			.andExpect(jsonPath("$.name", is("Mockito with Souvik")))
			.andExpect(jsonPath("$.publisher", is("Dutta Publisher")))
			.andExpect(jsonPath("$.author", is("Souvik Dutta")));
			}
		);
	}
	
	@Test
	void updateBook_invalid() throws Exception {
		when(bookClient.updateBook(Mockito.anyInt(),Mockito.any(BookDTO.class))).thenThrow(BookNotFoundException.class);
		RequestBuilder reqBuilder = MockMvcRequestBuilders
				.put("/library/books/2")
				.content(mapper.writeValueAsString(this.bookDTO))
				.contentType(MediaType.APPLICATION_JSON);
		mockMvc.perform(reqBuilder)
		.andExpect(status().isNotFound());
	}
	
	@Test
	public void deleteBook() {
		assertDoesNotThrow(() -> {
			doNothing().when(libraryService).releaseIssuedBook(Mockito.anyInt());
			when(bookClient.deleteBook(Mockito.anyInt())).thenReturn(Mockito.anyString());
			RequestBuilder reqBuilder = MockMvcRequestBuilders
							.delete("/library/books/1")
							.contentType(MediaType.APPLICATION_JSON);
			mockMvc.perform(reqBuilder)
					.andExpect(status().isOk());
		});
	}
	
	@Test
	public void deleteBook_invalid() throws Exception {
		
		doThrow(BookNotFoundException.class).when(bookClient).deleteBook(Mockito.anyInt());
		RequestBuilder reqBuilder = MockMvcRequestBuilders
						.delete("/library/books/2")
						.contentType(MediaType.APPLICATION_JSON);
		mockMvc.perform(reqBuilder)
				.andExpect(status().isNotFound());
		
	}
	
	@Test
	void getAllUsers() {
		assertDoesNotThrow(()-> {
			when(userClient.getAllUsers()).thenReturn(ResponseEntity.ok(this.users));
			RequestBuilder reqBuilder = MockMvcRequestBuilders
					.get("/library/users")
					.content(mapper.writeValueAsString(this.users))
					.contentType(MediaType.APPLICATION_JSON);
			mockMvc.perform(reqBuilder)
			.andExpect(jsonPath("$[0].username", is("user1")))
			.andExpect(jsonPath("$[0].email", is("souvik@gmail.com")))
			.andExpect(jsonPath("$[0].name", is("Souvik Dutta")));
			}
		);
	}
	
	@Test
	void addUser() {
		assertDoesNotThrow(()-> {
			when(userClient.adduser(Mockito.any(UserDTO.class))).thenReturn(ResponseEntity.ok(this.user));
			RequestBuilder reqBuilder = MockMvcRequestBuilders
					.post("/library/users")
					.content(mapper.writeValueAsString(this.user))
					.contentType(MediaType.APPLICATION_JSON);
			mockMvc.perform(reqBuilder)
			.andExpect(jsonPath("$.username", is("user1")))
			.andExpect(jsonPath("$.email", is("souvik@gmail.com")))
			.andExpect(jsonPath("$.name", is("Souvik Dutta")));
			}
		);
	}
	
	@Test
	void deleteUserAndHisBooks() {
		assertDoesNotThrow(() -> {
			doNothing().when(libraryService).deleteUser(Mockito.anyString());
			when(userClient.deleteUser(Mockito.anyString())).thenReturn(Mockito.anyString());
			RequestBuilder reqBuilder = MockMvcRequestBuilders
							.delete("/library/users/user1")
							.contentType(MediaType.APPLICATION_JSON);
			mockMvc.perform(reqBuilder)
					.andExpect(status().isOk());
		});
	}
	
	@Test
	void deleteUserAndHisBooks_invalid() throws Exception {
		
		doThrow(UserNotFoundException.class).when(userClient).deleteUser(Mockito.anyString());
		RequestBuilder reqBuilder = MockMvcRequestBuilders
						.delete("/library/users/user1")
						.contentType(MediaType.APPLICATION_JSON);
		mockMvc.perform(reqBuilder)
				.andExpect(status().isNotFound());
		
	}
	
	@Test
	void updateUserDetails() {
		assertDoesNotThrow(()-> {
			when(userClient.updateUser(Mockito.anyString(),Mockito.any(UserDTO.class))).thenReturn(ResponseEntity.ok(this.user));
			RequestBuilder reqBuilder = MockMvcRequestBuilders
					.put("/library/users/user1")
					.content(mapper.writeValueAsString(this.user))
					.contentType(MediaType.APPLICATION_JSON);
			mockMvc.perform(reqBuilder)
			.andExpect(jsonPath("$.username", is("user1")))
			.andExpect(jsonPath("$.email", is("souvik@gmail.com")))
			.andExpect(jsonPath("$.name", is("Souvik Dutta")));
			}
		);
	}
	
	@Test
	void issueBookToUser() {
		assertDoesNotThrow(()->{
			when(userClient.getUser(Mockito.anyString())).thenReturn(ResponseEntity.ok(user));
			when(bookClient.getBook(Mockito.anyInt())).thenReturn(ResponseEntity.ok(bookDTO));
			when(libraryService.issueABookToAUser(Mockito.anyString(), Mockito.anyInt())).thenReturn(libraryDTO);
			
			RequestBuilder reqBuilder = MockMvcRequestBuilders
					.post("/library/users/user1/books/1")
					.content(mapper.writeValueAsString(this.libraryDTO))
					.contentType(MediaType.APPLICATION_JSON);
			
			mockMvc.perform(reqBuilder)
			.andExpect(jsonPath("$.id", is(1)))
			.andExpect(jsonPath("$.username", is("souvik001")))
			.andExpect(jsonPath("$.bookId", is(11)));
			
		});
	}
	
	@Test
	void issueBookToUser_invalid1() throws Exception {
			when(userClient.getUser(Mockito.anyString())).thenThrow(UserNotFoundException.class);
			
			RequestBuilder reqBuilder = MockMvcRequestBuilders
					.post("/library/users/user1/books/1")
					.content(mapper.writeValueAsString(this.libraryDTO))
					.contentType(MediaType.APPLICATION_JSON);
			
			mockMvc.perform(reqBuilder)
			.andExpect(status().isNotFound());
	}
	
	@Test
	void issueBookToUser_invalid2() throws Exception {
			when(userClient.getUser(Mockito.anyString())).thenReturn(ResponseEntity.ok(user));
			when(bookClient.getBook(Mockito.anyInt())).thenThrow(BookNotFoundException.class);
			
			RequestBuilder reqBuilder = MockMvcRequestBuilders
					.post("/library/users/user1/books/1")
					.content(mapper.writeValueAsString(this.libraryDTO))
					.contentType(MediaType.APPLICATION_JSON);
			
			mockMvc.perform(reqBuilder)
			.andExpect(status().isNotFound());
	}
	
	@Test
	void issueBookToUser_invalid3() throws Exception {
			when(userClient.getUser(Mockito.anyString())).thenReturn(ResponseEntity.ok(user));
			when(bookClient.getBook(Mockito.anyInt())).thenReturn(ResponseEntity.ok(bookDTO));
			when(libraryService.issueABookToAUser(Mockito.anyString(), Mockito.anyInt())).thenThrow(IssuedMaxNumberOfBooksException.class);
			
			RequestBuilder reqBuilder = MockMvcRequestBuilders
					.post("/library/users/user1/books/1")
					.content(mapper.writeValueAsString(this.libraryDTO))
					.contentType(MediaType.APPLICATION_JSON);
			
			mockMvc.perform(reqBuilder)
			.andExpect(status().isConflict());
	}
	
	@Test
	void releaseBookForUser() {
		assertDoesNotThrow(()->{
			doNothing().when(libraryService).releaseBookForUser(Mockito.anyString(), Mockito.anyInt());
			RequestBuilder reqBuilder = MockMvcRequestBuilders
					.delete("/library/users/user1/books/1")
					.contentType(MediaType.APPLICATION_JSON);
			mockMvc.perform(reqBuilder)
			.andExpect(status().isOk());
		});
	}
	
	@Test
	void releaseBookForUser_invalid() {
		assertDoesNotThrow(()->{
			doThrow(BookNotFoundException.class).when(libraryService).releaseBookForUser(Mockito.anyString(), Mockito.anyInt());
			RequestBuilder reqBuilder = MockMvcRequestBuilders
					.delete("/library/users/user1/books/1")
					.contentType(MediaType.APPLICATION_JSON);
			mockMvc.perform(reqBuilder)
			.andExpect(status().isNotFound());
		});
	}
	
	@Test
	void viewUserProfile() {
		
		assertDoesNotThrow(()->{
			when(userClient.getUser(Mockito.anyString())).thenReturn(ResponseEntity.ok(user));
			when(libraryService.getBookIdsFromUser(Mockito.anyString())).thenReturn(List.of(1));
			when(bookClient.getBook(Mockito.anyInt())).thenReturn(ResponseEntity.ok(bookDTO));
					
			RequestBuilder reqBuilder = MockMvcRequestBuilders
					.get("/library/users/user1")
					.content(mapper.writeValueAsString(this.mpp))
					.contentType(MediaType.APPLICATION_JSON);
			
			mockMvc.perform(reqBuilder)
			.andExpect(status().isOk());
			
		});
	}
	
	@Test
	void viewUserProfile_invalid1() {
		
		assertDoesNotThrow(()->{
			when(userClient.getUser(Mockito.anyString())).thenThrow(UserNotFoundException.class);
					
			RequestBuilder reqBuilder = MockMvcRequestBuilders
					.get("/library/users/user1")
					.content(mapper.writeValueAsString(this.mpp))
					.contentType(MediaType.APPLICATION_JSON);
			
			mockMvc.perform(reqBuilder)
			.andExpect(status().isNotFound());
			
		});
	}
	
	@Test
	void viewUserProfile_invalid2() {
		
		assertDoesNotThrow(()->{
			when(userClient.getUser(Mockito.anyString())).thenReturn(ResponseEntity.ok(user));
			when(libraryService.getBookIdsFromUser(Mockito.anyString())).thenReturn(List.of(1));
			when(bookClient.getBook(Mockito.anyInt())).thenThrow(BookNotFoundException.class);
					
			RequestBuilder reqBuilder = MockMvcRequestBuilders
					.get("/library/users/user1")
					.content(mapper.writeValueAsString(this.mpp))
					.contentType(MediaType.APPLICATION_JSON);
			
			mockMvc.perform(reqBuilder)
			.andExpect(status().isNotFound());
			
		});
	}
	
	@Test
	void viewUserProfile_FeignExceptionTest() {
		
		assertDoesNotThrow(()->{
			when(userClient.getUser(Mockito.anyString())).thenReturn(ResponseEntity.ok(user));
			when(libraryService.getBookIdsFromUser(Mockito.anyString())).thenReturn(List.of(1));
			when(bookClient.getBook(Mockito.anyInt())).thenReturn(ResponseEntity.ok(bookDTO));
					
			RequestBuilder reqBuilder = MockMvcRequestBuilders
					.get("/library/users/user1/rr")
					.content(mapper.writeValueAsString(this.mpp))
					.contentType(MediaType.APPLICATION_JSON);
			
			mockMvc.perform(reqBuilder)
			.andExpect(status().isNotFound());
			
		});
	}
	
	
}
