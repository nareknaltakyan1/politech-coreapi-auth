package com.politech.core.auth.infrastructure.common.validation;

import org.springframework.http.HttpStatus;

public final class ValidationConstants
{

	public static final HttpStatus DEFAULT_INVALID_HTTP_STATUS = HttpStatus.BAD_REQUEST;

	private ValidationConstants()
	{
	}
}
