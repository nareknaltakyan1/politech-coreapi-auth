package com.politech.core.auth.domain.model.users;

import java.util.Locale;

public class UpdateUserDetailsBuilder
{
	private String name;
	private String email;
	private String phoneNumber;
	private Locale locale;
	private Boolean waitForSync;

	public UpdateUserDetails build()
	{
		return new UpdateUserDetails(name, email, phoneNumber, locale, waitForSync);
	}

	public UpdateUserDetailsBuilder withName(final String name)
	{
		this.name = name;
		return this;
	}

	public UpdateUserDetailsBuilder withEmail(final String email)
	{
		this.email = email;
		return this;
	}

	public UpdateUserDetailsBuilder withPhoneNumber(final String phoneNumber)
	{
		this.phoneNumber = phoneNumber;
		return this;
	}

	public UpdateUserDetailsBuilder withLocale(final Locale locale)
	{
		this.locale = locale;
		return this;
	}

	public UpdateUserDetailsBuilder withWaitForSync(final Boolean waitForSync)
	{
		this.waitForSync = waitForSync;
		return this;
	}
}
