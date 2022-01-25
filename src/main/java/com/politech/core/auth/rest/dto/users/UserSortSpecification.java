package com.politech.core.auth.rest.dto.users;


import com.politech.core.auth.infrastructure.common.presentation.rest.dto.SortSpecification;

public enum UserSortSpecification implements SortSpecification
{
	ID("id"), NAME("name"), EMAIL("email");

	final String property;

	UserSortSpecification(final String property)
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
