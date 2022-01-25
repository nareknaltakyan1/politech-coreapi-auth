package com.politech.core.auth.rest.dto.tokens;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class OneTimeLoginTokenResponseDto
{
	private String token;
	private int expiresInSeconds;

	public OneTimeLoginTokenResponseDto()
	{
	}

	public OneTimeLoginTokenResponseDto(final String token, final int expiresInSeconds)
	{
		this.token = token;
		this.expiresInSeconds = expiresInSeconds;
	}

	public String getToken()
	{
		return token;
	}

	public void setToken(String token)
	{
		this.token = token;
	}

	public int getExpiresInSeconds()
	{
		return expiresInSeconds;
	}

	public void setExpiresInSeconds(int expiresInSeconds)
	{
		this.expiresInSeconds = expiresInSeconds;
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
		final OneTimeLoginTokenResponseDto that = (OneTimeLoginTokenResponseDto) o;
		return new EqualsBuilder().append(getToken(), that.getToken()).append(getExpiresInSeconds(), that.getExpiresInSeconds()).isEquals();
	}

	@Override
	public int hashCode()
	{
		return new HashCodeBuilder(17, 23).append(getToken()).append(getExpiresInSeconds()).toHashCode();
	}

	@Override
	public String toString()
	{
		return new ToStringBuilder(this).append("token", getToken()).append("expiresInSeconds", getExpiresInSeconds()).toString();
	}
}
