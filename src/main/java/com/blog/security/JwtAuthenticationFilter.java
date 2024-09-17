package com.blog.security;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private CustomUserDetailService customUserDetailService;

	@Autowired
	private JwtTokenHelper jwtTokenHelper;

	private Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
	        throws ServletException, IOException {
		
		
		  String path = request.getRequestURI();
		    if (path.startsWith("/v3/api-docs") || path.startsWith("/swagger-ui")) {
		    	filterChain.doFilter(request, response); // Skip JWT check for Swagger
		        return;
		    }

	        // Get the Authorization header
	        final String authorizationHeader = request.getHeader("Authorization");

	        String username = null;
	        String jwtToken = null;

	        // Check if the Authorization header contains a Bearer token
	        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
	            jwtToken = authorizationHeader.substring(7);
	            try {
	                username = jwtTokenHelper.extractUsername(jwtToken);
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        } else {
	            System.out.println("JWT Token does not begin with Bearer String");
	        }

	        // Once we get the token, validate it
	        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

	            UserDetails userDetails = this.customUserDetailService.loadUserByUsername(username);

	            // If the token is valid, configure Spring Security to manually set authentication
	            if (jwtTokenHelper.validateToken(jwtToken, userDetails)) {

	                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
	                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

	                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

	                // Set the authentication in the context
	                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
	            }
	        }

	        filterChain.doFilter(request, response);
	    }
}
