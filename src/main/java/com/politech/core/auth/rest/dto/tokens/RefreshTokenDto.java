package com.politech.core.auth.rest.dto.tokens;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.constraints.NotBlank;

public class RefreshTokenDto
{
	@NotBlank
	private String token;

	public String getToken()
	{
		return token;
	}

	public void setToken(final String token)
	{
		this.token = token;
	}

	@Override
	public String toString()
	{
		return new ToStringBuilder(this).append("token", token).toString();
	}
}
