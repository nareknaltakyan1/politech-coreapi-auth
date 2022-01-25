package com.politech.core.auth.domain.model.users;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "GLOBAL")
public class GlobalUserPermission extends UserPermission
{

	public GlobalUserPermission()
	{
		super(PermissionType.GLOBAL);
	}
}
