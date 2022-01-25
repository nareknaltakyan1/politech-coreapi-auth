package com.politech.core.auth.rest.dto.users;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import javax.validation.constraints.Email;
import java.util.Locale;

@Value
public class UpdateUserDto
{
	String name;

	@Email
	String email;

	String phoneNumber;

	Locale locale;

	Boolean waitForSync;

	@JsonCreator
	public UpdateUserDto(@JsonProperty("name") final String name, @JsonProperty("email") final String email,
		@JsonProperty("phoneNumber") final String phoneNumber, @JsonProperty("locale") final Locale locale,
		@JsonProperty("waitForSync") final Boolean waitForSync)
	{
		this.name = name;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.locale = locale;
		this.waitForSync = waitForSync;
	}
}
