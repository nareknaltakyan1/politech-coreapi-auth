package com.politech.core.auth.rest.dto.organizations;

import java.time.LocalDateTime;

public class OrganizationDto
{
	private Long id;
	private String name;
	private LocalDateTime created;

	public Long getId()
	{
		return id;
	}

	public void setId(final Long id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(final String name)
	{
		this.name = name;
	}

	public LocalDateTime getCreated()
	{
		return created;
	}

	public void setCreated(final LocalDateTime created)
	{
		this.created = created;
	}
}
