package com.blog.payloads;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class JwtAuthRequest {
	private String userName;
	private String password;
}
