package com.politech.core.auth.rest.exception;

import com.politech.core.auth.infrastructure.common.validation.ErrorCode;
import org.springframework.util.StringUtils;

import java.util.Arrays;

public enum AuthErrorCode implements ErrorCode
{
	UNKNOWN("unknown"), LOCKED_USER_ACCOUNT("User account was locked"), BAD_CREDENTIALS("Provided credentials are wrong"),
	INVALID_PASSWORD_FORMAT("Invalid password format"), INVALID_EMAIL_FORMAT("Email format is not valid"), NOT_EXISTING_USER("User not exists"),
	INVALID_TOKEN("Invalid reset token"), NULL_VALUE("Value should not be null"), EXPIRED_TOKEN("Expired token"),
	UNIQUE_CONSTRAINT("Given value not unique"), PASSWORD_MISMATCH("Password is not matching with expected one"),
	BLANK_VALUE("Value should not be null/empty");

	private final String message;

	AuthErrorCode(final String message)
	{
		this.message = message;
	}

	public static AuthErrorCode fromValue(final String value)
	{
		if (StringUtils.hasText(value))
		{
			return AuthErrorCode.UNKNOWN;
		}
		return Arrays.stream(AuthErrorCode.values()).filter(it -> value.equalsIgnoreCase(it.name())).findFirst().orElse(AuthErrorCode.UNKNOWN);
	}

	public String getMessage()
	{
		return message;
	}
}
