package com.blog.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.blog.entities.User;
import com.blog.exceptions.EmailResourceNotFoundException;
import com.blog.repositories.UserRepo;

@Service
public class CustomUserDetailService implements UserDetailsService{

	@Autowired
	private UserRepo userRepo;
	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		
		//load user from database by email as userName
		User user= this.userRepo.findByEmail(userName).orElseThrow(()->new EmailResourceNotFoundException("User", "user email", userName));
		
		
		return user;
	}
	
	
}
