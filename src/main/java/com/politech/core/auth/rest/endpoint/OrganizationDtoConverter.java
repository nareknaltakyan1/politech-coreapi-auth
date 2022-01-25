package com.politech.core.auth.rest.endpoint;

import com.politech.core.auth.domain.model.organizations.Organization;
import com.politech.core.auth.rest.dto.organizations.OrganizationDto;
import org.springframework.stereotype.Component;

@Component
class OrganizationDtoConverter
{
	OrganizationDto convertToDto(final Organization org)
	{
		final OrganizationDto dto = new OrganizationDto();
		dto.setCreated(org.getCreated());
		dto.setId(org.getId());
		dto.setName(org.getName());
		return dto;
	}
}
