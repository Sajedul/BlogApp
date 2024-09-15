package com.blog.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.blog.payloads.ApiResponse;

//@ControllerAdvice
//centralize exception handling across multiple controllers
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiResponse> resourceNotFoundExceptionHandler(ResourceNotFoundException e) {
		
		String message = e.getMessage();
		ApiResponse apiResponse= new ApiResponse(message,false);
		return new ResponseEntity<ApiResponse>(apiResponse,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(EmailResourceNotFoundException.class)
	public ResponseEntity<ApiResponse> emailResourceNotFoundExceptionHandler(EmailResourceNotFoundException e) {
		
		String message = e.getMessage();
		ApiResponse apiResponse= new ApiResponse(message,false);
		return new ResponseEntity<ApiResponse>(apiResponse,HttpStatus.NOT_FOUND);
	}
	
	//The method returns a ResponseEntity containing a Map.
	//where the keys are field names and the values are the corresponding validation error messages.
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String,String>> handleMethodArgsNotValidException(MethodArgumentNotValidException ex){
		
		Map<String, String> response = new HashMap<>();
		
		// returns a list of all errors, which are then iterated over to extract error messages and associate them with the respective field names.
		
		ex.getBindingResult().getAllErrors().forEach((error)->{
			
			//String fieldName  =((Fielderror)error).getField();
			 String fieldName = error.getObjectName();
			 String message= error.getDefaultMessage();
			 
			 response.put(fieldName, message);
		});
		
		return new ResponseEntity<Map<String,String>>(response,HttpStatus.BAD_REQUEST);
		
	}
	
	
}
