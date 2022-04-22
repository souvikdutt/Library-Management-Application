package com.epam.librarymanager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LibraryDTO {
	private int id;
	private String username;
	private int bookId;
}
