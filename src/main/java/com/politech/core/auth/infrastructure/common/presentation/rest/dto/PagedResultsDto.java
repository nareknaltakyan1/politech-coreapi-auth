package com.politech.core.auth.infrastructure.common.presentation.rest.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public class PagedResultsDto<T>
{
	private final PaginationDto pagination;
	private final List<T> results;

	public PagedResultsDto(final Page<T> page)
	{
		results = page.getContent();
		pagination = new PaginationDto(page);
	}

	public PaginationDto getPagination()
	{
		return pagination;
	}

	public List<T> getResults()
	{
		return results;
	}
}
