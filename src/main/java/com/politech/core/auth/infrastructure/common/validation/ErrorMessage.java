package com.politech.core.auth.infrastructure.common.validation;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;

public class ErrorMessage
{
	private final HttpStatus httpStatus;
	private final String message;
	private final ErrorCode code;
	private final ErrorField field;

	public ErrorMessage(final HttpStatus httpStatus, final ErrorCode code, final String message)
	{
		Assert.notNull(httpStatus, "HTTP status must be provided");
		Assert.hasText(message, "Message must be provided");
		assertErrorCodeIsNotNull(code);
		this.httpStatus = httpStatus;
		this.code = code;
		this.message = message;
		field = null;
	}

	public ErrorMessage(final HttpStatus httpStatus, final ErrorCode code, final ErrorField field)
	{
		Assert.notNull(httpStatus, "HTTP status must be provided");
		Assert.notNull(code, "Error code should not be null");
		assertErrorCodeIsNotNull(code);
		this.httpStatus = httpStatus;
		this.code = code;
		this.message = code.getMessage();
		this.field = field;
	}

	public ErrorMessage(final ErrorCode code, final ErrorField field)
	{
		assertErrorCodeIsNotNull(code);
		this.httpStatus = ValidationConstants.DEFAULT_INVALID_HTTP_STATUS;
		this.code = code;
		this.message = code.getMessage();
		this.field = field;
	}

	public ErrorMessage(final ErrorCode code, final String message)
	{
		Assert.hasText(message, "Message must be provided");
		assertErrorCodeIsNotNull(code);
		this.httpStatus = ValidationConstants.DEFAULT_INVALID_HTTP_STATUS;
		this.code = code;
		this.message = message;
		field = null;
	}

	public ErrorMessage(final ErrorCode code)
	{
		assertErrorCodeIsNotNull(code);
		this.httpStatus = ValidationConstants.DEFAULT_INVALID_HTTP_STATUS;
		this.code = code;
		this.message = code.getMessage();
		field = null;
	}

	public HttpStatus getHttpStatus()
	{
		return httpStatus;
	}

	public String getMessage()
	{
		return message;
	}

	public ErrorCode getCode()
	{
		return code;
	}

	public ErrorField getField()
	{
		return field;
	}

	private static void assertErrorCodeIsNotNull(final ErrorCode code)
	{
		Assert.notNull(code, "Error code should not be null");
	}

	@Override
	public boolean equals(final Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o == null || getClass() != o.getClass())
		{
			return false;
		}
		final ErrorMessage that = (ErrorMessage) o;
		return new EqualsBuilder().append(httpStatus, that.httpStatus).append(message, that.message).append(code, that.code).append(field, that.field)
			.isEquals();
	}

	@Override
	public int hashCode()
	{
		return new HashCodeBuilder(17, 37).append(httpStatus).append(message).append(code).append(field).toHashCode();
	}

	@Override
	public String toString()
	{
		return new ToStringBuilder(this).append("httpStatus", httpStatus).append("message", message).append("code", code).append("field", field)
			.toString();
	}
}
