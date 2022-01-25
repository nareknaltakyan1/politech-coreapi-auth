package com.politech.core.auth.rest.dto.organizations;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class GetAllOrganizationsRequestDto
{

	private String name;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate createdFrom;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate createdTo;

	private LocalDateTime updatedAfter;

	public String getName()
	{
		return name;
	}

	public void setName(final String name)
	{
		this.name = name;
	}

	public LocalDate getCreatedFrom()
	{
		return createdFrom;
	}

	public void setCreatedFrom(final LocalDate createdFrom)
	{
		this.createdFrom = createdFrom;
	}

	public LocalDate getCreatedTo()
	{
		return createdTo;
	}

	public void setCreatedTo(final LocalDate createdTo)
	{
		this.createdTo = createdTo;
	}

	public LocalDateTime getUpdatedAfter()
	{
		return updatedAfter;
	}

	public void setUpdatedAfter(final LocalDateTime updatedAfter)
	{
		this.updatedAfter = updatedAfter;
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
		final GetAllOrganizationsRequestDto that = (GetAllOrganizationsRequestDto) o;
		return new EqualsBuilder().append(name, that.name).append(createdFrom, that.createdFrom).append(createdTo, that.createdTo)
			.append(updatedAfter, that.updatedAfter).isEquals();
	}

	@Override
	public int hashCode()
	{
		return new HashCodeBuilder(11, 51).append(name).append(createdFrom).append(createdTo).append(updatedAfter).toHashCode();
	}

	@Override
	public String toString()
	{
		return new ToStringBuilder(this).append("name", name).append("createdFrom", createdFrom).append("createdTo", createdTo)
			.append("updatedAfter", updatedAfter).toString();
	}
}
