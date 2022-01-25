package com.politech.core.auth.rest.dto.users;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginDto
{

	private String email;

	private String password;

	private String phoneNumber;
}
