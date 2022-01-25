package com.politech.core.auth.rest.dto.users;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RenewDynamicPasswordDto
{
	private String email;

	private String phoneNumber;
}
