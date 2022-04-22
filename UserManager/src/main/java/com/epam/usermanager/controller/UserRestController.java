package com.epam.usermanager.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.epam.usermanager.dto.UserDTO;
import com.epam.usermanager.exception.UserNotFoundException;
import com.epam.usermanager.service.UserService;

@RestController
public class UserRestController {

	@Autowired
	UserService service;
	
	@GetMapping("/users")
	public ResponseEntity<List<UserDTO>> getAllUsers() {
		return  ResponseEntity.ok(service.getAllUsers());
	}

	@GetMapping("/users/{username}")
	public ResponseEntity<UserDTO> getUser(@Valid @PathVariable String username) throws UserNotFoundException {
		return new ResponseEntity<>(service.getUserByUsername(username),HttpStatus.OK);
	}

	@PostMapping("/users")
	public ResponseEntity<UserDTO> adduser(@Valid @RequestBody UserDTO userDTO) {
		return new ResponseEntity<>(service.addUser(userDTO), HttpStatus.OK);
	}

	@DeleteMapping("/users/{username}")
	public String deleteUser(@Valid @PathVariable String username) throws UserNotFoundException {
		service.deleteUser(username);
		return username + " deleted successfully";
	}

	@PutMapping("/users/{username}")
	public ResponseEntity<UserDTO> updateUser(@Valid @PathVariable String username, @RequestBody UserDTO userDTO)
			throws UserNotFoundException {
		return new ResponseEntity<>(service.updateUser(username, userDTO), HttpStatus.OK);
	}
	
}
