package com.politech.core.auth.infrastructure.common.presentation.rest.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class PaginationRequest
{

	private Integer page;

	private Integer size;

	public PaginationRequest()
	{
		// Default
	}

	public int getPage()
	{
		return page == null ? 0 : page;
	}

	public void setPage(int page)
	{
		this.page = page;
	}

	public int getSize()
	{
		return size == null ? 10 : size;
	}

	public void setSize(int size)
	{
		this.size = size;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == this)
		{
			return true;
		}
		if (obj == null || obj.getClass() != getClass())
		{
			return false;
		}
		final PaginationRequest that = (PaginationRequest) obj;
		return new EqualsBuilder().append(this.page, that.page).append(this.size, that.size).isEquals();
	}

	@Override
	public int hashCode()
	{
		return new HashCodeBuilder().append(page).append(size).toHashCode();
	}

	@Override
	public String toString()
	{
		return new ToStringBuilder(this).append("page", page).append("size", size).toString();
	}
}
