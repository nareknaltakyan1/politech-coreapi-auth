package com.politech.core.auth.domain.model.users;

@SuppressWarnings("serial")
public class PasswordMismatchException extends RuntimeException
{

	private final String fieldName;

	public PasswordMismatchException(final String fieldName)
	{
		super("Provided password is not valid");
		this.fieldName = fieldName;
	}

	public String getFieldName()
	{
		return fieldName;
	}
}
