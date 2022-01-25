package com.politech.core.auth.rest.exception;

@SuppressWarnings("serial")
public class InvalidPasswordFormatException extends RuntimeException
{

	private final String field;

	public InvalidPasswordFormatException(final String field)
	{
		super("Invalid password format");
		this.field = field;
	}

	public String getField()
	{
		return field;
	}
}
