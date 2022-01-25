package com.politech.core.auth.infrastructure.common.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtAuthenticationToken extends AbstractAuthenticationToken
{

	private static final long serialVersionUID = 1426486290800977881L;

	private final JwtUser user;
	private final String jwtToken;

	public JwtAuthenticationToken(JwtUser user, final String jwtToken, Collection<? extends GrantedAuthority> authorities)
	{
		super(authorities);
		this.user = user;
		this.jwtToken = jwtToken;
		setAuthenticated(true);
	}

	@Override
	public Object getCredentials()
	{
		return jwtToken;
	}

	@Override
	public Object getPrincipal()
	{
		return user;
	}

}
