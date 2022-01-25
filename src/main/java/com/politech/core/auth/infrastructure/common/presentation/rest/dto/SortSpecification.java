package com.politech.core.auth.infrastructure.common.presentation.rest.dto;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;

public interface SortSpecification
{
	String DELIMITER = ";";

	String property();

	static Sort toSort(final String[] sortOptions, final SortSpecification[] specifications)
	{
		if (Objects.isNull(sortOptions) || Objects.isNull(specifications))
		{
			return Sort.by(new ArrayList<>());
		}
		final Sort.Order[] orders = Arrays.stream(sortOptions).map(each ->
		{
			final String[] options = each.split(DELIMITER);
			if (options.length != 2 || !containsSpecificationCode(options[0], specifications))
			{
				return null;
			}
			final String field = options[0];
			final Direction direction = Direction.valueOf(options[1].toUpperCase(Locale.getDefault()));
			if (direction.isAscending())
			{
				return Sort.Order.asc(field);
			}
			return Sort.Order.desc(field);

		}).filter(Objects::nonNull).toArray(Sort.Order[]::new);
		return Sort.by(orders);
	}

	static boolean containsSpecificationCode(final String code, final SortSpecification[] specifications)
	{
		return Arrays.stream(specifications).anyMatch(it -> it.property().equalsIgnoreCase(code));
	}
}