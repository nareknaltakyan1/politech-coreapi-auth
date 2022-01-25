package com.politech.core.auth.rest.endpoint;

import com.politech.core.auth.domain.model.users.User;
import com.politech.core.auth.rest.dto.users.UserDto;
import org.springframework.stereotype.Component;

@Component
class UserDtoConverter
{
	UserDto convertToDto(final User user)
	{
		final UserDto dto = new UserDto();
		dto.setAccountState(user.getAccountState());
		dto.setCreated(user.getCreated());
		dto.setEmail(user.getEmail());
		dto.setEmailVerified(user.isEmailVerified());
		dto.setId(user.getId());
		dto.setInvalidLoginAttempts(user.getFailedLoginAttempts().size());
		dto.setLastLogin(user.getLastLogin());
		dto.setLocale(user.getLocale());
		dto.setName(user.getName());
		return dto;
	}
}
