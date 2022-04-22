package com.epam.bookmanager.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {
	
	private int id;
	@NotBlank
	@NotNull
	private String name;
	@NotBlank
	@NotNull
	private String publisher;
	@NotBlank
	@NotNull
	private String author;
	
}
