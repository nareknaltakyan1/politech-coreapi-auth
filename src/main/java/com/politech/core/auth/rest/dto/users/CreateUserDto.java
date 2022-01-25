package com.politech.core.auth.rest.dto.users;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import javax.validation.constraints.Email;
import java.util.Locale;

@Value
public class CreateUserDto
{
	String name;

	@Email
	String email;

	String phoneNumber;

	String password;

	Boolean generatePassword;

	Locale locale;

	Boolean waitForSync;

	Boolean sendEmail;

	@JsonCreator

	public CreateUserDto(@JsonProperty("name") final String name, @JsonProperty("email") final String email,
		@JsonProperty("phoneNumber") final String phoneNumber, @JsonProperty("password") final String password,
		@JsonProperty("generatePassword") final Boolean generatePassword, @JsonProperty("locale") final Locale locale,
		@JsonProperty("waitForSync") final Boolean waitForSync, @JsonProperty("sendEmail") final Boolean sendEmail)
	{
		this.name = name;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.password = password;
		this.generatePassword = generatePassword;
		this.locale = locale;
		this.waitForSync = waitForSync;
		this.sendEmail = sendEmail;
	}
}
