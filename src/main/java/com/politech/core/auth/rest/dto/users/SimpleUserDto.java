package com.politech.core.auth.rest.dto.users;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class SimpleUserDto
{

	private Long id;
	private String name;

	public SimpleUserDto()
	{
	}

	public SimpleUserDto(final Long id, final String name)
	{
		this.id = id;
		this.name = name;
	}

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

	@Override
	public boolean equals(final Object obj)
	{
		if (obj == this)
		{
			return true;
		}
		if (obj == null || obj.getClass() != getClass())
		{
			return false;
		}
		final var that = (SimpleUserDto) obj;
		return new EqualsBuilder().append(this.id, that.id).append(this.name, that.name).isEquals();
	}

	@Override
	public int hashCode()
	{
		return new HashCodeBuilder().append(id).append(name).toHashCode();
	}

	@Override
	public String toString()
	{
		return new ToStringBuilder(this).append("id", id).append("name", name).toString();
	}
}
