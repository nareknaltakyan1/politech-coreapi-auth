package com.politech.core.auth.rest.exception;

import com.fasterxml.jackson.annotation.JsonValue;
import com.politech.core.auth.infrastructure.common.validation.ErrorField;
import org.springframework.util.StringUtils;

import java.util.Arrays;

public enum AuthErrorField implements ErrorField
{
	UNKNOWN("unknown"), EMAIL("email"), OLD_PASSWORD("oldPassword"), NEW_PASSWORD("newPassword"), PASSWORD_RESET_TOKEN("passwordResetToken"),
	NAME("name"), PASSWORD("password"), REFRESH_TOKEN("token"), PHONE_NUMBER("phone number");

	private final String code;

	AuthErrorField(final String code)
	{
		this.code = code;
	}

	public static AuthErrorField fromCode(final String code)
	{
		if (StringUtils.hasText(code))
		{
			return UNKNOWN;
		}
		return Arrays.stream(AuthErrorField.values()).filter(it -> code.equalsIgnoreCase(it.getCode())).findFirst().orElse(UNKNOWN);
	}

	@JsonValue
	public String getCode()
	{
		return code;
	}

}
