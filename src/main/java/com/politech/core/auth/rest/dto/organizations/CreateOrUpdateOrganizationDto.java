package com.politech.core.auth.rest.dto.organizations;

import javax.validation.constraints.NotNull;

public class CreateOrUpdateOrganizationDto
{
	@NotNull
	private String name;

	private boolean waitForSync;

	public String getName()
	{
		return name;
	}

	public void setName(final String name)
	{
		this.name = name;
	}

	public boolean isWaitForSync()
	{
		return waitForSync;
	}

	public void setWaitForSync(boolean waitForSync)
	{
		this.waitForSync = waitForSync;
	}
}
