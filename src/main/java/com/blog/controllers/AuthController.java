package com.blog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.payloads.JwtAuthRequest;
import com.blog.payloads.JwtAuthResponse;
import com.blog.payloads.UserDto;
import com.blog.security.CustomUserDetailService;
import com.blog.security.JwtTokenHelper;
import com.blog.services.UserService;

@RestController
@RequestMapping("/api/v1/auth/")
public class AuthController {
	@Autowired
	private JwtTokenHelper jwtTokenHelper;
	@Autowired
	private CustomUserDetailService customUserDetailService;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private UserService userService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@PostMapping("/login")
	public ResponseEntity<JwtAuthResponse> createToken(@RequestBody JwtAuthRequest request) {
		
		try {
            // Authenticate the user
            this.doAuthenticate(request.getUsername(), request.getPassword());

            // Load user details
            UserDetails userDetails = this.customUserDetailService.loadUserByUsername(request.getUsername());

            // Generate JWT token
            String token = this.jwtTokenHelper.generateToken(userDetails);

            // Create and return response
            JwtAuthResponse response = new JwtAuthResponse();
            response.setToken(token);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (BadCredentialsException e) {
            // Handle invalid credentials
            throw new BadCredentialsException("Invalid Username or Password");
        }
		
	}
	
	private void doAuthenticate(String username, String password) {
	    System.out.println("Attempting to authenticate user: " + username);
	    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
	    try {
	        authenticationManager.authenticate(authenticationToken);
	        System.out.println("Authentication successful for user: " + username);
	    } catch (BadCredentialsException e) {
	        System.out.println("Authentication failed for user: " + username);
	        throw new BadCredentialsException("Invalid Username or Password");
	    }
	}

	    @ExceptionHandler(BadCredentialsException.class)
	    public ResponseEntity<String> handleBadCredentialsException(BadCredentialsException ex) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
	    }
    
    @PostMapping("/register")
    public ResponseEntity<UserDto>registerUser(@RequestBody UserDto userDto){
    	
    	userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
    	UserDto registedUser = this.userService.registerNewUser(userDto);
    	
    	return new ResponseEntity<UserDto>(registedUser,HttpStatus.CREATED);
    }

}
