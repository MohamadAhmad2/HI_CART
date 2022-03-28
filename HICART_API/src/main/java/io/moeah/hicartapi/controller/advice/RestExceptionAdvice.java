package io.moeah.hicartapi.controller.advice;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.moeah.hicartapi.exception.AlreadyExistsException;
import io.moeah.hicartapi.exception.NotFoundException;
import io.moeah.hicartapi.exception.SellerHasProductsException;
import io.moeah.hicartapi.model.ErrorResponse;

@RestControllerAdvice
public class RestExceptionAdvice {

	private static final String VALIDATION_ERROR_MSG = "Validation Error Occured";

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {

		ErrorResponse _return = new ErrorResponse(HttpStatus.BAD_REQUEST, VALIDATION_ERROR_MSG);
		Map<String, Object> validationErrors = new HashMap<>();

		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			validationErrors.put(fieldName, errorMessage);
		});

		_return.setErrors(validationErrors);

		return ResponseEntity.badRequest().body(_return);
	}

	@ExceptionHandler(AlreadyExistsException.class)
	public ResponseEntity<ErrorResponse> handleAlreadyExistsExceptions(AlreadyExistsException ex) {
		ErrorResponse _return = new ErrorResponse(HttpStatus.CONFLICT, ex.getMessage());
		return ResponseEntity.status(HttpStatus.CONFLICT).body(_return);
	}

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<ErrorResponse> handleNotFoundExceptions(NotFoundException ex) {
		ErrorResponse _return = new ErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(_return);
	}

	@ExceptionHandler(SellerHasProductsException.class)
	public ResponseEntity<ErrorResponse> handleNotFoundExceptions(SellerHasProductsException ex) {
		ErrorResponse _return = new ErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(_return);
	}

}
