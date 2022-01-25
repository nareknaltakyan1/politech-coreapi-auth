package com.politech.core.auth.domain.model.users;

import java.util.Locale;
import java.util.Objects;

import static org.springframework.util.Assert.isTrue;

public class UpdateUserDetails
{
	private final String name;
	private final String email;
	private final String phoneNumber;
	private final Locale locale;
	private final boolean waitForSync;

	public UpdateUserDetails(final String name, final String email, final String phoneNumber, final Locale locale, final Boolean waitForSync)
	{
		isTrue(email != null || phoneNumber != null, "Email or phone number must be exist");
		this.name = name;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.locale = locale;
		this.waitForSync = Objects.requireNonNullElse(waitForSync, Boolean.FALSE);
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

	public Locale getLocale()
	{
		return locale;
	}

	public boolean isWaitForSync()
	{
		return waitForSync;
	}
}
