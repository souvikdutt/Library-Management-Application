package com.epam.librarymanager.error;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.epam.librarymanager.model.ErrorResponse;

import feign.FeignException;

@RestControllerAdvice
public class RestExceptionHandler {

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> feignException(FeignException e, WebRequest request) {
		ErrorResponse message = new ErrorResponse(LocalDateTime.now(), HttpStatus.NOT_FOUND, e.getMessage(),
				request.getContextPath());
		
		System.out.println(e.responseBody().get());
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorResponse> issuedMaxNumberOfBooksException(IssuedMaxNumberOfBooksException e, WebRequest request) {
		ErrorResponse message = new ErrorResponse(LocalDateTime.now(), HttpStatus.CONFLICT, e.getMessage(),
				request.getContextPath());
		return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorResponse> userNotFoundException(UserNotFoundException e, WebRequest request) {
		ErrorResponse message = new ErrorResponse(LocalDateTime.now(), HttpStatus.NOT_FOUND, e.getMessage(),
				request.getContextPath());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorResponse> userNotFoundException(BookNotFoundException e, WebRequest request) {
		ErrorResponse message = new ErrorResponse(LocalDateTime.now(), HttpStatus.NOT_FOUND, e.getMessage(),
				request.getContextPath());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message); 
	}

}
