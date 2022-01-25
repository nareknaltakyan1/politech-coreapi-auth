package com.politech.core.auth.rest.dto.users;

import com.politech.core.auth.domain.model.users.UserAccountState;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Locale;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class UserDto extends SimpleUserDto
{
	private String email;
	private String phoneNumber;
	private LocalDateTime created;
	private LocalDateTime lastLogin;
	private int invalidLoginAttempts;
	private UserAccountState accountState;
	private boolean emailVerified;
	private Locale locale;
}
