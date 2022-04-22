package com.epam.usermanager.service;

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

import com.epam.usermanager.dto.UserDTO;
import com.epam.usermanager.exception.UserNotFoundException;
import com.epam.usermanager.model.User;
import com.epam.usermanager.reporsitory.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

	@InjectMocks
	UserServiceImpl userService;
	@Mock
	UserRepository userRepo;
	@Mock
	ModelMapper modelMapper;
	
	private UserDTO userDTO;
	private User user;
	private List<UserDTO> userDTOS;
	private List<User> users;
	
	@BeforeEach
	void setup() {
		this.userDTO = new UserDTO("user1", "souvik@gmail.com", "Souvik Dutta");
		this.userDTOS = new ArrayList<>();
		this.userDTOS.add(userDTO);
		this.user = new User("user1", "souvik@gmail.com", "Souvik Dutta");
		this.users = new ArrayList<>();
		this.users.add(user);
	}
	
	@Test
	void getAllBooks() {
		when(userRepo.findAll()).thenReturn(users);
		assertTrue(userService.getAllUsers().size()>0);
	}
	
	@Test
	void getUser() throws UserNotFoundException {
		when(userRepo.findById(Mockito.anyString())).thenReturn(Optional.of(user));
		when(modelMapper.map(Mockito.any(User.class), Mockito.any())).thenReturn(this.userDTO);
		assertEquals(this.userDTO, userService.getUserByUsername("user1"));
	}
	
	@Test
	void getUser_invalid() throws UserNotFoundException {
		when(userRepo.findById(Mockito.anyString())).thenThrow(UserNotFoundException.class);
		assertThrows(UserNotFoundException.class,()-> userService.getUserByUsername("user1"));
	}
	
	@Test
	void addUser() {
		when(modelMapper.map(Mockito.any(UserDTO.class), Mockito.any())).thenReturn(this.user);
		when(userRepo.save(Mockito.any())).thenReturn(user);
		when(modelMapper.map(Mockito.any(User.class), Mockito.any())).thenReturn(this.userDTO);
		assertEquals(this.userDTO, userService.addUser(userDTO));
	}
	
	@Test
	void addUserWithCustomId() {
		User user1 = new User();
		user1.setName("Souvik");
		user1.setEmail("dutta@gmail.com");
		userRepo.save(user1);
	}
	
	@Test
	void updateUser() {
		when(userRepo.findById(Mockito.anyString())).thenReturn(Optional.of(user));
		when(userRepo.save(Mockito.any())).thenReturn(user);
		when(modelMapper.map(Mockito.any(User.class), Mockito.any())).thenReturn(this.userDTO);
		assertEquals(this.userDTO, userService.updateUser("user1",userDTO));
	}
	
	@Test
	void updateUser_invalid() {
		when(userRepo.findById(Mockito.anyString())).thenThrow(UserNotFoundException.class);
		assertThrows(UserNotFoundException.class,()->  userService.updateUser("user1",userDTO));
	}
	
	@Test
	void deleteUser() throws UserNotFoundException {
		when(userRepo.existsById(Mockito.anyString())).thenReturn(true);
		doNothing().when(userRepo).deleteById(Mockito.anyString());
		userService.deleteUser("user1");
	}
	
	@Test
	void deleteBook_invalid() throws UserNotFoundException {
		when(userRepo.existsById(Mockito.anyString())).thenReturn(false);
		assertThrows(UserNotFoundException.class,()-> userService.deleteUser("user1"));
	}
	
}
