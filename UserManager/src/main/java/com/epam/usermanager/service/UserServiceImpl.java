package com.epam.usermanager.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epam.usermanager.dto.UserDTO;
import com.epam.usermanager.exception.UserNotFoundException;
import com.epam.usermanager.model.User;
import com.epam.usermanager.reporsitory.UserRepository;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	UserRepository userRepository;
	@Autowired
	ModelMapper modelMapper;
	
	@Override
	public List<UserDTO> getAllUsers() {
		return userRepository.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
	}

	@Override
	public UserDTO getUserByUsername(String username) throws UserNotFoundException {
		User user = userRepository.findById(username).orElseThrow(()->new UserNotFoundException());
		return convertToDto(user);
	}

	@Override
	public UserDTO addUser(UserDTO userDTO) {
		User user = userRepository.save(convertToEntity(userDTO));
		return convertToDto(user);
	}

	@Override
	public void deleteUser(String username) throws UserNotFoundException {
		if(userRepository.existsById(username))
			userRepository.deleteById(username);
		else
			throw new UserNotFoundException();
	}

	@Override
	public UserDTO updateUser(String username, UserDTO userDTO) throws UserNotFoundException {
		User existedUser = userRepository.findById(username).orElseThrow(()->new UserNotFoundException());
		
		if(Objects.nonNull(userDTO.getEmail()) && !"".equalsIgnoreCase(userDTO.getEmail())) {
			existedUser.setEmail(userDTO.getEmail());
		}
		
		if(Objects.nonNull(userDTO.getName()) && !"".equalsIgnoreCase(userDTO.getName())) {
			existedUser.setName(userDTO.getName());
		}
		
		return convertToDto(userRepository.save(existedUser));
	}

	private UserDTO convertToDto(User user) {
		UserDTO userDTO = modelMapper.map(user, UserDTO.class);
		return userDTO;
	}
	
	private User convertToEntity(UserDTO userDTO) {
		User user = modelMapper.map(userDTO, User.class);
		return user;
	}
	
}
