package com.politech.core.auth.infrastructure.common.presentation.rest.dto;

import org.springframework.data.domain.Page;

public class PaginationDto
{
	private final int currentPage;
	private final int pageSize;
	private final int totalPages;
	private final long totalResults;

	public PaginationDto(final Page<?> page)
	{
		currentPage = page.getNumber();
		pageSize = page.getSize();
		totalPages = page.getTotalPages();
		totalResults = page.getTotalElements();
	}

	public int getCurrentPage()
	{
		return currentPage;
	}

	public int getPageSize()
	{
		return pageSize;
	}

	public int getTotalPages()
	{
		return totalPages;
	}

	public long getTotalResults()
	{
		return totalResults;
	}
}
