package com.politech.core.auth.domain.model.organizations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class GetAllOrganizationsDetails
{

	private String name;
	private LocalDate createdFrom;
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

	public LocalDateTime getCreatedFrom()
	{
		return createdFrom == null ? null : createdFrom.atStartOfDay();
	}

	public void setCreatedFrom(final LocalDate createdFrom)
	{
		this.createdFrom = createdFrom;
	}

	public LocalDateTime getCreatedTo()
	{
		return createdTo == null ? null : LocalDateTime.of(createdTo, LocalTime.now());
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
}
