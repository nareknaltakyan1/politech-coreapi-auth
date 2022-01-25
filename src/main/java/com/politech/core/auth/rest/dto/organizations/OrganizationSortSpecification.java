package com.politech.core.auth.rest.dto.organizations;


import com.politech.core.auth.infrastructure.common.presentation.rest.dto.SortSpecification;

public enum OrganizationSortSpecification implements SortSpecification
{

	NAME("name");

	final String property;

	OrganizationSortSpecification(final String property)
	{
		this.property = property;
	}

	@Override
	public String property()
	{
		return property;
	}

	@Override
	public String toString()
	{
		return property();
	}
}
