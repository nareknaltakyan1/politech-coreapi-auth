package com.politech.core.auth.rest.dto.users;

public class AccessTokenDto
{

	private final String accessToken;
	private final long expiresInSeconds;
	private final String refreshToken;

	public AccessTokenDto(final String accessToken, final long expiresInSeconds)
	{
		this(accessToken, expiresInSeconds, null);
	}

	public AccessTokenDto(final String accessToken, final long expiresInSeconds, final String refreshToken)
	{
		this.accessToken = accessToken;
		this.expiresInSeconds = expiresInSeconds;
		this.refreshToken = refreshToken;
	}

	public String getAccessToken()
	{
		return accessToken;
	}

	public long getExpiresInSeconds()
	{
		return expiresInSeconds;
	}

	public String getRefreshToken()
	{
		return refreshToken;
	}
}
