package com.epam.librarymanager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserDTO {
	private String username;
	private String email;
	private String name;
}
