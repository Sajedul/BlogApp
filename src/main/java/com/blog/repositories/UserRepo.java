package com.blog.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blog.entities.User;

public interface UserRepo extends JpaRepository<User, Integer> {

	//find user by email and use it as userName
	 Optional<User> findByEmail(String email);
	
}
