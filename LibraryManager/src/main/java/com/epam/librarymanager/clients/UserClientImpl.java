package com.epam.librarymanager.clients;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.epam.librarymanager.dto.UserDTO;
import com.epam.librarymanager.error.UserNotFoundException;

@Service
public class UserClientImpl implements UserClient {

	@Override
	public ResponseEntity<List<UserDTO>> getAllUsers() {
		return ResponseEntity.ok(List.of(new UserDTO("demo1", "abc@email.com", "Recon")));
	}

	@Override
	public ResponseEntity<UserDTO> getUser(@Valid String username) throws UserNotFoundException {
		return ResponseEntity.ok(new UserDTO("demo1", "abc@email.com", "Recon"));
	}

	@Override
	public ResponseEntity<UserDTO> adduser(@Valid UserDTO user) {
		return ResponseEntity.ok(new UserDTO("demo1", "abc@email.com", "Recon"));
	}

	@Override
	public ResponseEntity<UserDTO> updateUser(@Valid String username, UserDTO user) {
		return ResponseEntity.ok(new UserDTO("demo1", "abc@email.com", "Recon"));
	}

	@Override
	public String deleteUser(@Valid String username) throws UserNotFoundException {
		return "Deleting from fallback..";
	}

}
