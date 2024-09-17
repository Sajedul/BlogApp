package com.blog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.authentication.configurers.userdetails.DaoAuthenticationConfigurer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.blog.security.CustomUserDetailService;
import com.blog.security.JwtAuthenticationEntryPoint;
import com.blog.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@EnableWebMvc
public class SecurityConfig{
	
	public static final String[] PUBLIC_URLS= {
    	    "/api/users/**",                 // Public user endpoints
    	    "/api/**",
    	    "/api/v1/auth/login",            // Login endpoint
    	    "/api/v1/auth/**",               // Other authentication-related endpoints
    	    "/v3/api-docs",               // OpenAPI 3.x docs
    	    "/swagger-resources/**",         // Swagger resources
    	    "/swagger-ui/**",                // Swagger UI
    	    "/webjars/**",                   // Static resources used by Swagger
    	    "/swagger-ui.html"               // Swagger HTML entry point (if used)
    	};

	    @Autowired
	    private JwtAuthenticationFilter jwtAuthenticationFilter;

	    @Autowired
	    private CustomUserDetailService customUserDetailService;

	    @Bean
	    public PasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }

	    @Bean
	    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
	        return authenticationConfiguration.getAuthenticationManager();
	    }

	    @Bean
	    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	        http
	            .csrf().disable()
	            .authorizeHttpRequests((authorize) -> 
	                authorize
	                    .requestMatchers("/api/v1/auth/login").permitAll()
	                    .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
	                    .requestMatchers(PUBLIC_URLS).permitAll()
	                    .requestMatchers(HttpMethod.GET).permitAll()
	                    .anyRequest().authenticated()
	            )
	            .sessionManagement(session -> 
	                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	            );

	        // Add the JWT filter
	        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

	        return http.build();
	    }

	    @Bean
	    public DaoAuthenticationProvider authenticationProvider() {
	        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
	        authProvider.setUserDetailsService(customUserDetailService);
	        authProvider.setPasswordEncoder(passwordEncoder());
	        return authProvider;
	    }
	
	
//	@Bean
//	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//	    return http.csrf(csrf -> csrf.disable())
//	             .authorizeHttpRequests(authz -> authz
//	                 .requestMatchers(PUBLIC_URLS).permitAll()
//	                 .requestMatchers(HttpMethod.GET).permitAll() // Optionally include this
//	                 .anyRequest().authenticated()
//	             )
//	             .exceptionHandling(ex -> ex.authenticationEntryPoint(this.point))
//	             .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//	             .addFilterBefore(this.filter, UsernamePasswordAuthenticationFilter.class)
//	             .build();
//	}
}
