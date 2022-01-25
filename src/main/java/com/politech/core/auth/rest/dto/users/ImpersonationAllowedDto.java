package com.politech.core.auth.rest.dto.users;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class ImpersonationAllowedDto
{
	private boolean allowed;

	public ImpersonationAllowedDto()
	{
	}

	public ImpersonationAllowedDto(final boolean allowed)
	{
		this.allowed = allowed;
	}

	public boolean isAllowed()
	{
		return allowed;
	}

	public void setAllowed(boolean allowed)
	{
		this.allowed = allowed;
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
		final ImpersonationAllowedDto that = (ImpersonationAllowedDto) o;
		return new EqualsBuilder().append(isAllowed(), that.isAllowed()).isEquals();
	}

	@Override
	public int hashCode()
	{
		return new HashCodeBuilder(17, 23).append(isAllowed()).toHashCode();
	}

	@Override
	public String toString()
	{
		return new ToStringBuilder(this).append("allowed", isAllowed()).toString();
	}
}
