package com.epam.librarymanager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BookDTO {
	private int id;
	private String name;
	private String publisher;
	private String author;
}
