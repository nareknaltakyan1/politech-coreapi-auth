package com.politech.core.auth.domain.service;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class PersistenceUtilityService
{

	@SuppressWarnings("unchecked")
	public <T> T unProxy(final T entity)
	{
		Assert.notNull(entity, "Entity should not be null");
		return (T) Hibernate.unproxy(entity);
	}
}
