package com.blog.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.blog.config.AppConstants;
import com.blog.entities.Role;
import com.blog.entities.User;
import com.blog.payloads.UserDto;
import com.blog.repositories.RoleRepo;
import com.blog.repositories.UserRepo;
import com.blog.services.UserService;
import com.blog.exceptions.ResourceNotFoundException;


@Service
public class UserServiceImpl implements UserService{
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private RoleRepo roleRepo;

	@Override
	public UserDto createUser(UserDto userDto) {
		
		User user = this.dtoToUser(userDto);//convert userDto type data into User type data
		User savedUser = this.userRepo.save(user);//save this data in the database
		
		return this.userToDto(savedUser);//As function return type is UserDto so we need to return UserDto type data
	}

	@Override
	public UserDto upadteUser(UserDto userDto, Integer userId) {
		User user = this.userRepo.findById(userId)
				.orElseThrow(()-> new ResourceNotFoundException("User","id",userId));
		
		user.setName(userDto.getName());
		user.setEmail(userDto.getEmail());
		user.setPassword(userDto.getPassword());
		user.setAbout(userDto.getAbout());
		
		User updateUser = this.userRepo.save(user);
		UserDto userDto1 = this.userToDto(updateUser);
		
		return userDto1;
	}

	@Override
	public UserDto getUserById(Integer userId) {
		
		User user = this.userRepo.findById(userId)
		.orElseThrow(()->new ResourceNotFoundException("User","Id",userId));
		
		
		return this.userToDto(user);
	}

	@Override
	public void deleteUser(Integer userId) {
		
		//find it from database then delete it
	    User user	= this.userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User","Id",userId));
	    this.userRepo.delete(user);
	}

	@Override
	public List<UserDto> getAllUsers() {
		//get user list from database
		List<User>users = this.userRepo.findAll();
		
		//convert user to userDto because method return type is UserDto
		
		List<UserDto> userDtos = users.stream().map(user->this.userToDto(user)).collect(Collectors.toList());
		
		return userDtos;
	}
	
	
	//conversion of dto to user using modelMapper
	//use of ModelMapper to convert an instance of one to another
	
	public User dtoToUser(UserDto userDto) {
	
		
//		  User user = new User(); 
//		  user.setId(userDto.getId());
//		  user.setName(userDto.getName()); 
//		  user.setEmail(userDto.getEmail());
//		  user.setPassword(userDto.getPassword());
//		  user.setAbout(userDto.getAbout());
		
		User user = this.modelMapper.map(userDto, User.class);
		 
		return user;	
	}
	
	//conversion of user to dto
	
	public UserDto userToDto(User user) {
		
//		UserDto  userDto = new UserDto();
//		userDto.setId(user.getId());
//		userDto.setName(user.getName());
//		userDto.setEmail(user.getEmail());
//		userDto.setPassword(user.getPassword());
//		userDto.setAbout(user.getAbout());
		
		UserDto userDto= this.modelMapper.map(user, UserDto.class);
		
		return userDto;
	}

	@Override
	public UserDto registerNewUser(UserDto userDto) {
		
		User user = this.modelMapper.map(userDto, User.class);
		user.setPassword(this.passwordEncoder.encode(user.getPassword()));
		
		Role role = this.roleRepo.findById(AppConstants.NORMAl_USER).get();
		user.getRoles().add(role);
		User newUser = this.userRepo.save(user);
		return this.modelMapper.map(newUser, UserDto.class);
	}

}
