package com.politech.core.auth.rest.dto.tokens;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class OneTimeLoginTokenRequestDto
{
	private String token;

	public String getToken()
	{
		return token;
	}

	public void setToken(String token)
	{
		this.token = token;
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
		final OneTimeLoginTokenRequestDto that = (OneTimeLoginTokenRequestDto) o;
		return new EqualsBuilder().append(getToken(), that.getToken()).isEquals();
	}

	@Override
	public int hashCode()
	{
		return new HashCodeBuilder(17, 23).append(getToken()).toHashCode();
	}

	@Override
	public String toString()
	{
		return new ToStringBuilder(this).append("token", getToken()).toString();
	}
}
