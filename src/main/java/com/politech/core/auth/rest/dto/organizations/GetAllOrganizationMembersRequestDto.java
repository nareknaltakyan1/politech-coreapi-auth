package com.politech.core.auth.rest.dto.organizations;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class GetAllOrganizationMembersRequestDto
{

	private String name;
	private String email;

	public GetAllOrganizationMembersRequestDto()
	{
		// Default
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
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
		final GetAllOrganizationMembersRequestDto that = (GetAllOrganizationMembersRequestDto) obj;
		return new EqualsBuilder().append(this.name, that.name).append(this.email, that.email).isEquals();
	}

	@Override
	public int hashCode()
	{
		return new HashCodeBuilder().append(name).append(email).toHashCode();
	}

	@Override
	public String toString()
	{
		return new ToStringBuilder(this).append("name", name).append("email", email).toString();
	}
}
