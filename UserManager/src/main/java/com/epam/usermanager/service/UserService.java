package com.epam.usermanager.service;

import java.util.List;

import com.epam.usermanager.dto.UserDTO;
import com.epam.usermanager.exception.UserNotFoundException;

public interface UserService {

	List<UserDTO> getAllUsers();
	UserDTO getUserByUsername(String username) throws UserNotFoundException;
	UserDTO addUser(UserDTO userDTO);
	void deleteUser(String username) throws UserNotFoundException;
	UserDTO updateUser(String username, UserDTO userDTO) throws UserNotFoundException;
	
}
