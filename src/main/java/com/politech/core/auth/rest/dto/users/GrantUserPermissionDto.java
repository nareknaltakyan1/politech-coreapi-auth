package com.politech.core.auth.rest.dto.users;


import com.politech.core.auth.domain.model.users.Permission;

public class GrantUserPermissionDto
{
	private Permission permission;
	private Long organizationId;

	@Deprecated
	public Permission getRole()
	{
		return permission;
	}

	@Deprecated
	public void setRole(final Permission role)
	{
		this.permission = role;
	}

	public Permission getPermission()
	{
		return permission;
	}

	public void setPermission(final Permission role)
	{
		this.permission = role;
	}

	public Long getOrganizationId()
	{
		return organizationId;
	}

	public void setOrganizationId(final Long organizationId)
	{
		this.organizationId = organizationId;
	}
}
