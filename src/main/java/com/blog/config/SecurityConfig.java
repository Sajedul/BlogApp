package com.blog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.blog.security.CustomUserDetailService;

@Configuration
public class SecurityConfig{
	
	@Autowired
	private CustomUserDetailService userDetailsService;
	
	@Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        // user detail service for object:
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        // password encoder for object
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

        return daoAuthenticationProvider;
    }
	
	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
	 @Bean
	    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

	        // configuration
	        httpSecurity.authorizeHttpRequests(authorize -> {
	            // authorize.requestMatchers("/home", "/register", "/services").permitAll();
	            //authorize.requestMatchers("/user/**").authenticated();
	            authorize.anyRequest().permitAll();
	        });

	        return httpSecurity.build();

	    }

}
