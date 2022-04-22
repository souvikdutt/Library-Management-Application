package com.epam.usermanager.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.epam.usermanager.model.ErrorResponse;


@RestControllerAdvice
public class RestExceptionHandler {

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> userNotFoundException(UserNotFoundException e, WebRequest request) {
		ErrorResponse message = new ErrorResponse(LocalDateTime.now(), HttpStatus.NOT_FOUND, e.getMessage(),
				request.getContextPath());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public ErrorResponse invalidArgumentHandler(MethodArgumentNotValidException exception, WebRequest request) {
		
		StringBuilder message = new StringBuilder();
		exception.getAllErrors().forEach(err -> message.append(err.getDefaultMessage()).append(", "));
		ErrorResponse response = new ErrorResponse(LocalDateTime.now(),HttpStatus.BAD_REQUEST, message.toString(),request.getContextPath());
		return response;
		
	}

}
