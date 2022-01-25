package com.politech.core.auth.domain.service.organizations;

public interface OrganizationSynchronizer
{
	void synchronize(final boolean waitForCompletion);
}
