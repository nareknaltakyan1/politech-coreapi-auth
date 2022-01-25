package com.politech.core.auth.domain.service.organizations;

import com.politech.core.auth.domain.model.organizations.Organization;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface OrganizationRepository extends PagingAndSortingRepository<Organization, Long>, QuerydslPredicateExecutor<Organization>
{

	Optional<Organization> findByName(final String name);

}
