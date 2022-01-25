package com.politech.core.auth.domain.model.users;

@SuppressWarnings("serial")
public class UserNotExistsForEmailException extends RuntimeException
{

	private final String email;

	public UserNotExistsForEmailException(final String email, String message)
	{
		super(message);
		this.email = email;
	}

	public String getEmail()
	{
		return email;
	}
}
