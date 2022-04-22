package com.epam.librarymanager.model;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ErrorResponse {

	private LocalDateTime timeStamp;
	private HttpStatus status;
	private String message;
	private String path;
	
}
