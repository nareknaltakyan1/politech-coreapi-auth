package com.politech.core.auth.infrastructure.common.security;

import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class JwtUser implements Serializable
{
	private static final long serialVersionUID = -7539034411628035050L;

	private final Long id;
	private final String email;
	private final String name;
	private final Set<? extends GrantedAuthority> authorities;

	public JwtUser(final Long id, final String email, final String name, final Collection<? extends GrantedAuthority> authorities)
	{
		this.id = id;
		this.email = email;
		this.name = name;
		final Set<GrantedAuthority> temp = new HashSet<>();
		temp.addAll(authorities);
		this.authorities = Collections.unmodifiableSet(temp);
	}

	public Long getId()
	{
		return id;
	}

	public String getEmail()
	{
		return email;
	}

	public String getName()
	{
		return name;
	}

	public Set<? extends GrantedAuthority> getAuthorities()
	{
		return authorities;
	}
}
