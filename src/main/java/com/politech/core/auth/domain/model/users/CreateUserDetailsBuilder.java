package com.politech.core.auth.domain.model.users;

import java.util.Locale;

public class CreateUserDetailsBuilder
{
	private String name;
	private String email;
	private String phoneNumber;
	private String password;
	private Boolean generatePassword;
	private Locale locale;
	private Boolean waitForSync;
	private Boolean sendEmail;

	public CreateUserDetails build()
	{
		return new CreateUserDetails(name, email, phoneNumber, password, generatePassword, locale, waitForSync, sendEmail);
	}

	public CreateUserDetailsBuilder withName(final String name)
	{
		this.name = name;
		return this;
	}

	public CreateUserDetailsBuilder withEmail(final String email)
	{
		this.email = email;
		return this;
	}

	public CreateUserDetailsBuilder withPhoneNumber(final String phoneNumber)
	{
		this.phoneNumber = phoneNumber;
		return this;
	}

	public CreateUserDetailsBuilder withPassword(final String password)
	{
		this.password = password;
		return this;
	}

	public CreateUserDetailsBuilder withGeneratePassword(final Boolean generatePassword)
	{
		this.generatePassword = generatePassword;
		return this;
	}

	public CreateUserDetailsBuilder withLocale(final Locale locale)
	{
		this.locale = locale;
		return this;
	}

	public CreateUserDetailsBuilder withWaitForSync(final Boolean waitForSync)
	{
		this.waitForSync = waitForSync;
		return this;
	}

	public CreateUserDetailsBuilder withSendEmail(final Boolean sendEmail)
	{
		this.sendEmail = sendEmail;
		return this;
	}
}
