package com.politech.core.auth.rest.dto.users;

import com.politech.core.auth.infrastructure.common.validation.Password;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PasswordResetDto
{
	private String email;

	private String phoneNumber;

	private String oldPassword;

	private String passwordResetToken;

	@Password
	private String newPassword;
}
