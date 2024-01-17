package com.authentication.demo.exception;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	public ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		String message = ex.getMessage();
		List<String> details = new ArrayList<>();
		details.add("Request method not supported");
		ApiError errors = new ApiError(message, LocalDateTime.now());

		return ResponseEntity.status(status).body(errors);

	}

	@Override
	public ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		String message = ex.getMessage();
		List<String> details = new ArrayList<>();
		details.add("Media not supported");
		details.add(ex.getMessage());

		ApiError errors = new ApiError(message, LocalDateTime.now());
		return ResponseEntity.status(status).body(errors);
	}

	@Override
	public ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		String message = ex.getMessage();
		List<String> details = new ArrayList<>();
		details.add("Path Variable is missing ");

		ApiError errors = new ApiError(message, LocalDateTime.now());
		return ResponseEntity.status(status).body(errors);
	}

	@Override
	public ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		String message = ex.getMessage();
		List<String> details = new ArrayList<>();
		details.add("Request param is missing");

		ApiError errors = new ApiError(message, LocalDateTime.now());
		return ResponseEntity.status(status).body(errors);

	}

	@Override
	public ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status,
			WebRequest request) {
		String message = ex.getMessage();
		List<String> details = new ArrayList<>();
		details.add("MissMatch of type");

		ApiError errors = new ApiError(message, LocalDateTime.now());
		return ResponseEntity.status(status).body(errors);

	}

	@Override
	public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		String message = ex.getMessage();
		List<String> details = new ArrayList<>();
		details.add("Request body is not redable");

		ApiError errors = new ApiError(message, LocalDateTime.now());
		return ResponseEntity.status(status).body(errors);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleOther(Exception ex) {
		String message = ex.getMessage();
		List<String> details = new ArrayList<>();
		details.add("other exception");
		details.add(ex.getMessage());

		/*
		 * ApiError errors = new ApiError(message, details, HttpStatus.BAD_REQUEST,
		 * LocalDateTime.now());
		 */
		ApiError errors = new ApiError(message, LocalDateTime.now());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
	}

	@ExceptionHandler(InvalidCredentialsException.class)
	public ResponseEntity<Object> handleIdNotFoundException(InvalidCredentialsException ex) {
		String message = ex.getMessage();
		ApiError errors = new ApiError(message, LocalDateTime.now());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
	}

}
