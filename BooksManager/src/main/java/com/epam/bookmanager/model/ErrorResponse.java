package com.epam.bookmanager.model;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {

	private LocalDateTime timeStamp;
	private HttpStatus status;
	private String message;
	private String path;
	
}
