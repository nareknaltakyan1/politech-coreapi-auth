package com.politech.core.auth.domain.model.token;

@SuppressWarnings("serial")
public class InvalidTokenException extends RuntimeException
{

	public InvalidTokenException(final String message)
	{
		this(message, null);
	}

	public InvalidTokenException(final String message, final Exception ex)
	{
		super(message, ex);
	}

}
