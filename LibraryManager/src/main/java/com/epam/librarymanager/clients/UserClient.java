package com.epam.librarymanager.clients;

import java.util.List;

import javax.validation.Valid;

import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.epam.librarymanager.dto.UserDTO;
import com.epam.librarymanager.error.UserNotFoundException;

@FeignClient(name = "user-service", fallback = UserClientImpl.class)
@LoadBalancerClient(name = "user-service", configuration = UserClientImpl.class)
public interface UserClient {
	@GetMapping("/users")
	public ResponseEntity<List<UserDTO>> getAllUsers();
	
	@GetMapping("/users/{username}")
	public ResponseEntity<UserDTO> getUser(@Valid @PathVariable String username) throws UserNotFoundException;
	
	@PostMapping("/users")
	public ResponseEntity<UserDTO> adduser(@Valid @RequestBody UserDTO user);
	
	@PutMapping("/users/{username}")
	public ResponseEntity<UserDTO> updateUser(@Valid @PathVariable String username, @RequestBody UserDTO user);
	
	@DeleteMapping("/users/{username}")
	public String deleteUser(@Valid @PathVariable String username) throws UserNotFoundException;
}
