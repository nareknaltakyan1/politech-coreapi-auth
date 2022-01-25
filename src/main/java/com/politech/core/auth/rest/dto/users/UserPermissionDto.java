package com.politech.core.auth.rest.dto.users;

import com.politech.core.auth.domain.model.users.Permission;
import com.politech.core.auth.rest.dto.organizations.OrganizationDto;

public class UserPermissionDto
{
	private Long id;
	private Permission permission;
	private OrganizationDto organization;

	public Long getId()
	{
		return id;
	}

	public Permission getPermission()
	{
		return permission;
	}

	public OrganizationDto getOrganization()
	{
		return organization;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public void setPermission(Permission role)
	{
		this.permission = role;
	}

	public void setOrganization(OrganizationDto organization)
	{
		this.organization = organization;
	}

	@Deprecated
	public Permission getRole()
	{
		return permission;
	}

	@Deprecated
	public void setRole(Permission role)
	{
		this.permission = role;
	}
}
