package com.politech.core.auth.rest.endpoint;

import com.politech.core.auth.domain.model.organizations.GetAllOrganizationsDetails;
import com.politech.core.auth.rest.dto.organizations.GetAllOrganizationsRequestDto;
import org.springframework.stereotype.Component;

@Component
class GetAllOrganizationsDetailsConverter
{
	GetAllOrganizationsDetails convertToDetails(final GetAllOrganizationsRequestDto request)
	{
		final GetAllOrganizationsDetails pojo = new GetAllOrganizationsDetails();
		pojo.setCreatedFrom(request.getCreatedFrom());
		pojo.setCreatedTo(request.getCreatedTo());
		pojo.setName(request.getName());
		pojo.setUpdatedAfter(request.getUpdatedAfter());
		return pojo;
	}
}
