package com.politech.core.auth.domain.model.users;

import com.politech.core.auth.domain.model.organizations.Organization;
import org.springframework.util.Assert;

import javax.persistence.*;

@Entity
@DiscriminatorValue(value = "ORGANIZATIONAL")
public class OrganizationalUserPermission extends UserPermission
{

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "organizationId")
	private Organization organization;

	public OrganizationalUserPermission()
	{
		super(PermissionType.ORGANIZATIONAL);
	}

	public Organization getOrganization()
	{
		return organization;
	}

	public void setOrganization(final Organization organization)
	{
		Assert.notNull(organization, "Organization must be specified");
		this.organization = organization;
	}
}
