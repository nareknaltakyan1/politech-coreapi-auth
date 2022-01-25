package com.politech.core.auth.infrastructure.common.presentation.rest.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LocalTimeDto
{
	private LocalTime time;

	public LocalTimeDto()
	{
	}

	public LocalTimeDto(final String source)
	{
		if (StringUtils.isNotBlank(source))
		{
			time = LocalTime.parse(source, DateTimeFormatter.ISO_TIME);
		}
		else
		{
			time = null;
		}
	}

	public LocalTimeDto(final LocalTime time)
	{
		this.time = time;
	}

	@JsonValue
	public String getTimeAsString()
	{
		if (time != null)
		{
			return time.toString();
		}
		return null;
	}

	public LocalTime toLocalTime()
	{
		return time;
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
		final LocalTimeDto that = (LocalTimeDto) o;
		return new EqualsBuilder().append(time, that.time).isEquals();
	}

	@Override
	public int hashCode()
	{
		return new HashCodeBuilder(17, 37).append(time).toHashCode();
	}

	@Override
	public String toString()
	{
		return new ToStringBuilder(this).append("time", time).toString();
	}
}
