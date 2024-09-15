package com.blog.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.blog.payloads.ApiResponse;
import com.blog.payloads.UserDto;
import com.blog.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	//create UserDto type response Entity for avoiding direct access of or exposing of user data
	@PostMapping("/create")
	public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
		userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
		UserDto createdUser = this.userService.createUser(userDto);
		return new ResponseEntity<>(createdUser,HttpStatus.CREATED);
	}
	
	//update user
	
	@PutMapping("/{userId}")
	
	//@RequestBody UserDto userDto indicates that the incoming HTTP request body will be parsed into a UserDto object.
	public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto,
			                                 @PathVariable("userId")Integer userId) {
		
		UserDto updatedUser = this.userService.upadteUser(userDto, userId);
		
		return ResponseEntity.ok(updatedUser);
	}
	
	@DeleteMapping("/{userId}")
	//ResponseEntity<?> allows flexibility in the type of the response body.
	//indicates that the method can return a ResponseEntity of any type, but you're not specifying a concrete type for the response body.
	//It can return any type or no body at all
	public ResponseEntity<ApiResponse> deleteUser(@PathVariable("userId")Integer uid) {
		this.userService.deleteUser(uid);
		//return new ResponseEntity(Map.of("message","User Deleted Successfully"),HttpStatus.OK);
		
		//manually creating a ResponseEntity object with a custom response body (ApiResponse) and an HTTP status of 200 OK
		
		//return new ResponseEntity(new ApiResponse("User Deleted Successfully",true), HttpStatus.OK);
		
		ApiResponse response = new ApiResponse("User Deleted Successfully", true);
	    return new ResponseEntity<ApiResponse>(response, HttpStatus.OK);
	}
	
	//Get User
	@GetMapping("/")
	public ResponseEntity<List<UserDto>> getAllUser() {
		return ResponseEntity.ok(this.userService.getAllUsers());
	}
	
	//Get User
		@GetMapping("/{userId}")
		public ResponseEntity<UserDto> getSingleUser(@PathVariable("userId")Integer userId) {
			return ResponseEntity.ok(this.userService.getUserById(userId));
		}
	
}
