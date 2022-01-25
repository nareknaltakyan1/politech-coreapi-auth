package com.politech.core.auth.domain.model.token;

@SuppressWarnings("serial")
public class ExpiredTokenException extends RuntimeException
{

	public ExpiredTokenException(final String msg)
	{
		super(msg);
	}
}
