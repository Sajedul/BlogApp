package com.blog.payloads;

import java.util.HashSet;
import java.util.Set;

import com.blog.entities.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//use for transfer data and not expose user public or access directly
//it contain the same attribute as User
//bypassing the direct use of user entities
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

	private int id;
	@NotEmpty
	@Size(min=4,message="User name must be min of 4 charecters")
	private String name;
	@Email(message="Email Address is not valid")
	private String email;
	@NotEmpty
	@Size(min=3,max=10,message="Password must be of 3 chars and max of 10 character")
	private String password;
	@NotEmpty
	private String about;
	
	private Set<RoleDto> roles = new HashSet<>();
	
}
