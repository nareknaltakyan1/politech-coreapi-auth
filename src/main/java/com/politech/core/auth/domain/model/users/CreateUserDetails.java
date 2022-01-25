package com.politech.core.auth.domain.model.users;

import java.util.Locale;
import java.util.Objects;

import static org.springframework.util.Assert.isTrue;
import static org.springframework.util.Assert.notNull;

public class CreateUserDetails
{
	private final String name;
	private final String email;
	private final String phoneNumber;
	private final String password;
	private final boolean generatePassword;
	private final Locale locale;
	private final boolean waitForSync;
	private final boolean sendEmail;

	public CreateUserDetails(final String name, final String email, final String phoneNumber, final String password, final Boolean generatePassword,
		final Locale locale, final Boolean waitForSync, final Boolean sendEmail)
	{
		isTrue(email != null || phoneNumber != null, "Email or phone number must exist");
		this.generatePassword = Objects.requireNonNullElse(generatePassword, Boolean.FALSE);
		isTrue(password != null || this.generatePassword, "Password must be supplied if generatePassword is false");
		this.sendEmail = Objects.requireNonNullElse(sendEmail, Boolean.TRUE);
		this.waitForSync = Objects.requireNonNullElse(waitForSync, Boolean.FALSE);
		this.email = email;
		if (this.sendEmail)
		{
			notNull(email, "If sendEmail is true, email must exist");
		}
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.password = password;
		this.locale = locale;

	}

	public String getName()
	{
		return name;
	}

	public String getEmail()
	{
		return email;
	}

	public String getPhoneNumber()
	{
		return phoneNumber;
	}

	public String getPassword()
	{
		return password;
	}

	public boolean isGeneratePassword()
	{
		return generatePassword;
	}

	public Locale getLocale()
	{
		return locale;
	}

	public boolean isWaitForSync()
	{
		return waitForSync;
	}

	public boolean isSendEmail()
	{
		return sendEmail;
	}
}
