package com.blog.security;

import java.util.ArrayList;

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
	public UserDetails loadUserByUsername(String username) throws EmailResourceNotFoundException {
		
		//load user from database by email as userName
//		User user= this.userRepo.findByEmail(username).orElseThrow(()->new EmailResourceNotFoundException("User", "user email", username));
//		
//		
//		return user;
		
		User user= this.userRepo.findByEmail(username).orElseThrow(()->new EmailResourceNotFoundException("User", "user email", username));
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
	}
	
	
}
