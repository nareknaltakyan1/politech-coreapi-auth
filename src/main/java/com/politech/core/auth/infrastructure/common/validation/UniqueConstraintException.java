package com.politech.core.auth.infrastructure.common.validation;

public class UniqueConstraintException extends RuntimeException
{
	private final String fieldName;

	public UniqueConstraintException(final String fieldName)
	{
		this.fieldName = fieldName;
	}

	public String getFieldName()
	{
		return fieldName;
	}
}
