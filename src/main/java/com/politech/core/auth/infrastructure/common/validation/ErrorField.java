package com.politech.core.auth.infrastructure.common.validation;

import com.fasterxml.jackson.annotation.JsonValue;

public interface ErrorField
{
	@JsonValue
	String getCode();
}
