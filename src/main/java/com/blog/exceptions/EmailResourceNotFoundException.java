package com.blog.exceptions;

public class EmailResourceNotFoundException extends RuntimeException{
	
	String resourceName;
	String filedName;
	String fieldValue;
	public EmailResourceNotFoundException(String resourceName, String filedName, String fieldValue) {
		super(String.format("%s not found with %s : %s",resourceName,filedName,fieldValue));
		this.resourceName = resourceName;
		this.filedName = filedName;
		this.fieldValue = fieldValue;
	}

}
