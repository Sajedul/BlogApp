package com.blog.services;

import java.util.List;

import com.blog.payloads.UserDto;

public interface UserService {
	
	UserDto registerNewUser(UserDto user);
	
	UserDto createUser(UserDto user);
	UserDto upadteUser(UserDto user,Integer userId);
	UserDto getUserById(Integer userId);
	
	void deleteUser(Integer userId);
	
	List<UserDto> getAllUsers();
}
