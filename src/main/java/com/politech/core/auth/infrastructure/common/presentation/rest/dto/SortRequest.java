package com.politech.core.auth.infrastructure.common.presentation.rest.dto;

import java.util.Arrays;
import java.util.Objects;

public class SortRequest
{

	private String[] sort;

	public SortRequest()
	{
		// Default
	}

	public SortRequest(final String... sort)
	{
		this.sort = sort;
	}

	public String[] getSort()
	{
		if (Objects.isNull(sort))
		{
			sort = new String[] {};
		}
		return Arrays.copyOf(sort, sort.length);
	}

	public void setSort(final String[] sort)
	{
		this.sort = Arrays.copyOf(sort, sort.length);
	}

	public boolean isEmpty()
	{
		return sort == null || sort.length == 0;
	}
}
