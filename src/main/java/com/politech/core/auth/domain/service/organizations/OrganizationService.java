package com.politech.core.auth.domain.service.organizations;

import com.politech.core.auth.domain.model.organizations.GetAllOrganizationsDetails;
import com.politech.core.auth.domain.model.organizations.MutateOrganizationDetails;
import com.politech.core.auth.domain.model.organizations.Organization;
import com.politech.core.auth.domain.model.organizations.QOrganization;
import com.politech.core.auth.domain.service.system.SystemDateTimeService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class OrganizationService
{
	private static final String LIKE_PATTERN = "%%%s%%";

	@Autowired
	private OrganizationRepository repository;

	@Autowired
	private SystemDateTimeService systemDateTimeService;

//	@Autowired
//	private OrganizationSynchronizer organizationSynchronizer;

	@Transactional(readOnly = true)
	public Page<Organization> findAll(final GetAllOrganizationsDetails getAllOrganizationsDetails, final Pageable pageable)
	{
		final Predicate filter = buildFilter(getAllOrganizationsDetails);
		return repository.findAll(filter, pageable);
	}

	@Transactional
	public Organization createOrganization(final MutateOrganizationDetails details)
	{
		final Organization o = new Organization();
		o.setName(details.getName());
		o.setCreated(systemDateTimeService.getCurrentDateTime());
		o.setUpdated(systemDateTimeService.getCurrentDateTime());
		repository.save(o);
//		organizationSynchronizer.synchronize(details.isWaitForSync());
		return o;
	}

	@Transactional
	public void updateOrganization(final Long id, final MutateOrganizationDetails details)
	{
		final Organization org = findById(id);
		org.setName(details.getName());
		org.setUpdated(systemDateTimeService.getCurrentDateTime());
		repository.save(org);
//		organizationSynchronizer.synchronize(details.isWaitForSync());
	}

	@Transactional(readOnly = true)
	public Organization findById(final Long id)
	{
		final Optional<Organization> org = repository.findById(id);
		if (org.isEmpty())
		{
			throw new EntityNotFoundException(String.format("Could not find organization with id '%d'", id));
		}
		return org.get();
	}

	@Transactional(readOnly = true)
	public Organization findByName(final String name)
	{
		final Optional<Organization> org = repository.findByName(name);
		if (org.isEmpty())
		{
			throw new EntityNotFoundException(String.format("Could not find organization with name '%s'", name));
		}
		return org.get();
	}

	private Predicate buildFilter(final GetAllOrganizationsDetails getAllUsersPojo)
	{
		Assert.notNull(getAllUsersPojo, "Argument getAllUsersPojo should not be null");
		final BooleanBuilder filter = new BooleanBuilder();
		Optional.ofNullable(getAllUsersPojo.getName()).filter(StringUtils::hasText)
			.ifPresent(name -> filter.and(QOrganization.organization.name.likeIgnoreCase(String.format(LIKE_PATTERN, name))));
		Optional.ofNullable(getAllUsersPojo.getCreatedFrom())
			.ifPresent(createdFrom -> filter.and(QOrganization.organization.created.goe(createdFrom)));
		Optional.ofNullable(getAllUsersPojo.getCreatedTo()).ifPresent(createdTo -> filter.and(QOrganization.organization.created.loe(createdTo)));
		Optional.ofNullable(getAllUsersPojo.getUpdatedAfter())
			.ifPresent(updatedAfter -> filter.and(QOrganization.organization.created.gt(updatedAfter)));
		return filter;
	}
}
