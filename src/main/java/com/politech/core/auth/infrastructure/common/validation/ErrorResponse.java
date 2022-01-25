package com.politech.core.auth.infrastructure.common.validation;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ErrorResponse
{
	private final List<ErrorMessage> errors;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
	private final LocalDateTime timestamp;

	public ErrorResponse()
	{
		this.errors = new ArrayList<>();
		this.timestamp = LocalDateTime.now();
	}

	public ErrorResponse(final List<ErrorMessage> errors, final LocalDateTime timestamp)
	{
		this.errors = new ArrayList<>(errors);
		this.timestamp = timestamp;
	}

	public List<ErrorMessage> getErrors()
	{
		return Collections.unmodifiableList(this.errors);
	}

	public void addError(final ErrorMessage errorMessage)
	{
		this.errors.add(errorMessage);
	}

	public LocalDateTime getTimestamp()
	{
		return timestamp;
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
		final ErrorResponse that = (ErrorResponse) o;
		return new EqualsBuilder().append(errors, that.errors).append(timestamp, that.timestamp).isEquals();
	}

	@Override
	public int hashCode()
	{
		return new HashCodeBuilder(17, 37).append(errors).append(timestamp).toHashCode();
	}

	@Override
	public String toString()
	{
		return new ToStringBuilder(this).append("errors", errors).append("timestamp", timestamp).toString();
	}
}
