package com.politech.core.auth.rest.dto.users;

import com.politech.core.auth.domain.model.users.Permission;
import com.politech.core.auth.domain.model.users.PermissionType;

public class PermissionDto
{

	private Permission permission;

	private PermissionType type;

	public PermissionDto()
	{
		// Default
	}

	public PermissionDto(final Permission permission)
	{
		this.permission = permission;
		this.type = permission.getType();
	}

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

	public PermissionType getType()
	{
		return type;
	}

	public void setType(final PermissionType type)
	{
		this.type = type;
	}

	public Permission getPermission()
	{
		return permission;
	}

	public void setPermission(Permission permission)
	{
		this.permission = permission;
	}
}
