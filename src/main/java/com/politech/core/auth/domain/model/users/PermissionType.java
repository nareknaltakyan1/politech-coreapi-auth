package com.politech.core.auth.domain.model.users;

public enum PermissionType
{
	GLOBAL, ORGANIZATIONAL;

	public boolean isGlobal()
	{
		return this == GLOBAL;
	}

	public boolean isOrganizational()
	{
		return this == ORGANIZATIONAL;
	}
}
