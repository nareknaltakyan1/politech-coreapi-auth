package com.politech.core.auth.rest.endpoint;

import com.politech.core.auth.domain.model.organizations.MutateOrganizationDetails;
import com.politech.core.auth.domain.model.organizations.MutateOrganizationDetailsBuilder;
import com.politech.core.auth.rest.dto.organizations.CreateOrUpdateOrganizationDto;
import org.springframework.stereotype.Component;

@Component
public class MutateOrganizationDetailsConverter
{
	public MutateOrganizationDetails convert(final CreateOrUpdateOrganizationDto dto)
	{
		return new MutateOrganizationDetailsBuilder().withName(dto.getName()).withWaitForSync(dto.isWaitForSync()).build();
	}
}
