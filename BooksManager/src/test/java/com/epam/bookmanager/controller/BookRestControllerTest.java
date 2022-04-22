package com.epam.bookmanager.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.epam.bookmanager.dto.BookDTO;
import com.epam.bookmanager.exception.BookNotFoundException;
import com.epam.bookmanager.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest
public class BookRestControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	BookService bookService;

	private BookDTO bookDTO;
	private List<BookDTO> bookDtos;
	private ObjectMapper mapper;
	
	@BeforeEach
	void setup() {
		this.mapper = new ObjectMapper();
		this.bookDTO = new BookDTO(1,"Mockito with Souvik","Dutta Publisher","Souvik Dutta");
		this.bookDtos = new ArrayList<>();
		this.bookDtos.add(bookDTO);
	}
	
	@Test
	void addBook() {
		assertDoesNotThrow(()-> {
			when(bookService.addBook(Mockito.any(BookDTO.class))).thenReturn(this.bookDTO);
			RequestBuilder reqBuilder = MockMvcRequestBuilders
					.post("/books")
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
	void getAllBooks() {
		assertDoesNotThrow(()-> {
			when(bookService.getAllBooks()).thenReturn(this.bookDtos);
			RequestBuilder reqBuilder = MockMvcRequestBuilders
					.get("/books")
					.content(mapper.writeValueAsString(this.bookDtos))
					.contentType(MediaType.APPLICATION_JSON);
			mockMvc.perform(reqBuilder)
			.andExpect(jsonPath("$[0].id", is(1)))
			.andExpect(jsonPath("$[0].name", is("Mockito with Souvik")))
			.andExpect(jsonPath("$[0].publisher", is("Dutta Publisher")))
			.andExpect(jsonPath("$[0].author", is("Souvik Dutta")));
			}
		);
	}
	
	@Test
	void getBook() {
		assertDoesNotThrow(()-> {
			when(bookService.getBook(Mockito.anyInt())).thenReturn(this.bookDTO);
			RequestBuilder reqBuilder = MockMvcRequestBuilders
					.get("/books/1")
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
		when(bookService.getBook(Mockito.anyInt())).thenThrow(BookNotFoundException.class);
		RequestBuilder reqBuilder = MockMvcRequestBuilders
				.get("/books/2")
				.content(mapper.writeValueAsString(this.bookDTO))
				.contentType(MediaType.APPLICATION_JSON);
		mockMvc.perform(reqBuilder)
		.andExpect(status().isNotFound());
	}
	
	@Test
	public void deleteBook() {
		assertDoesNotThrow(() -> {
			doNothing().when(bookService).deleteBook(Mockito.anyInt());
			RequestBuilder reqBuilder = MockMvcRequestBuilders
							.delete("/books/1")
							.contentType(MediaType.APPLICATION_JSON);
			mockMvc.perform(reqBuilder)
					.andExpect(status().isOk());
		});
	}
	
	@Test
	public void deleteBook_invalid() throws Exception {
		
		doThrow(BookNotFoundException.class).when(bookService).deleteBook(Mockito.anyInt());
		RequestBuilder reqBuilder = MockMvcRequestBuilders
						.delete("/books/2")
						.contentType(MediaType.APPLICATION_JSON);
		mockMvc.perform(reqBuilder)
				.andExpect(status().isNotFound());
		
	}
	
	@Test
	void updateBook() {
		assertDoesNotThrow(()-> {
			when(bookService.updateBook(Mockito.anyInt(),Mockito.any(BookDTO.class))).thenReturn(this.bookDTO);
			RequestBuilder reqBuilder = MockMvcRequestBuilders
					.put("/books/1")
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
		when(bookService.updateBook(Mockito.anyInt(),Mockito.any(BookDTO.class))).thenThrow(BookNotFoundException.class);
		RequestBuilder reqBuilder = MockMvcRequestBuilders
				.put("/books/2")
				.content(mapper.writeValueAsString(this.bookDTO))
				.contentType(MediaType.APPLICATION_JSON);
		mockMvc.perform(reqBuilder)
		.andExpect(status().isNotFound());
	}
	
}
