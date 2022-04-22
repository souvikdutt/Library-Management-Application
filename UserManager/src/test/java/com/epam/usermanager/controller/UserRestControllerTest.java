package com.epam.usermanager.controller;

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

import com.epam.usermanager.dto.UserDTO;
import com.epam.usermanager.exception.UserNotFoundException;
import com.epam.usermanager.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest
public class UserRestControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	UserService userService;
	
	private UserDTO user;
	private List<UserDTO> users;
	private ObjectMapper mapper;
	
	@BeforeEach
	void setup() {
		this.mapper = new ObjectMapper();
		this.user = new UserDTO("user1", "souvik@gmail.com", "Souvik Dutta");
		this.users = new ArrayList<>();
		this.users.add(user);
	}
	
	@Test
	void addUser() {
		assertDoesNotThrow(()-> {
			when(userService.addUser(Mockito.any(UserDTO.class))).thenReturn(this.user);
			RequestBuilder reqBuilder = MockMvcRequestBuilders
					.post("/users")
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
	void getAllUsers() {
		assertDoesNotThrow(()-> {
			when(userService.getAllUsers()).thenReturn(this.users);
			RequestBuilder reqBuilder = MockMvcRequestBuilders
					.get("/users")
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
	void getUserByUsername() {
		assertDoesNotThrow(()-> {
			when(userService.getUserByUsername(Mockito.anyString())).thenReturn(this.user);
			RequestBuilder reqBuilder = MockMvcRequestBuilders
					.get("/users/user1")
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
	void getUserByUsername_invalid() throws Exception {
		when(userService.getUserByUsername(Mockito.anyString())).thenThrow(UserNotFoundException.class);
		RequestBuilder reqBuilder = MockMvcRequestBuilders
				.get("/users/user2")
				.content(mapper.writeValueAsString(this.user))
				.contentType(MediaType.APPLICATION_JSON);
		mockMvc.perform(reqBuilder)
		.andExpect(status().isNotFound());
	}
	
	@Test
	public void deleteUser() {
		assertDoesNotThrow(() -> {
			doNothing().when(userService).deleteUser(Mockito.anyString());
			RequestBuilder reqBuilder = MockMvcRequestBuilders
							.delete("/users/user1")
							.contentType(MediaType.APPLICATION_JSON);
			mockMvc.perform(reqBuilder)
					.andExpect(status().isOk());
		});
	}
	
	@Test
	public void deleteUser_invalid() throws Exception {
		
		doThrow(UserNotFoundException.class).when(userService).deleteUser(Mockito.anyString());
		RequestBuilder reqBuilder = MockMvcRequestBuilders
						.delete("/users/user1")
						.contentType(MediaType.APPLICATION_JSON);
		mockMvc.perform(reqBuilder)
				.andExpect(status().isNotFound());
		
	}
	
	@Test
	void updateUser() {
		assertDoesNotThrow(()-> {
			when(userService.updateUser(Mockito.anyString(),Mockito.any(UserDTO.class))).thenReturn(this.user);
			RequestBuilder reqBuilder = MockMvcRequestBuilders
					.put("/users/user1")
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
	void updateUser_invalid() throws Exception {
		when(userService.updateUser(Mockito.anyString(),Mockito.any(UserDTO.class))).thenThrow(UserNotFoundException.class);
		RequestBuilder reqBuilder = MockMvcRequestBuilders
				.put("/users/user1")
				.content(mapper.writeValueAsString(this.user))
				.contentType(MediaType.APPLICATION_JSON);
		mockMvc.perform(reqBuilder)
		.andExpect(status().isNotFound());
	}
	
}
