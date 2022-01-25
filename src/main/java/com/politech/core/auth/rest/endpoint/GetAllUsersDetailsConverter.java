package com.politech.core.auth.rest.endpoint;

import com.politech.core.auth.domain.model.users.GetAllUsersDetails;
import com.politech.core.auth.rest.dto.organizations.GetAllOrganizationMembersRequestDto;
import com.politech.core.auth.rest.dto.users.GetAllUsersRequest;
import org.springframework.stereotype.Component;

@Component
class GetAllUsersDetailsConverter
{
	GetAllUsersDetails convertToDetails(final Long organizationId, final GetAllOrganizationMembersRequestDto membersRequest)
	{
		final GetAllUsersDetails details = new GetAllUsersDetails();
		details.setName(membersRequest.getName());
		details.setEmail(membersRequest.getEmail());
		details.setOrganizationId(organizationId);
		return details;
	}

	GetAllUsersDetails convertToDetails(final GetAllUsersRequest filterUserDto)
	{
		final GetAllUsersDetails details = new GetAllUsersDetails();
		details.setAccountState(filterUserDto.getAccountState());
		details.setCreatedFrom(filterUserDto.getCreatedFrom());
		details.setCreatedTo(filterUserDto.getCreatedTo());
		details.setEmail(filterUserDto.getEmail());
		details.setEmailVerified(filterUserDto.getEmailVerified());
		details.setInvalidLoginAttempts(filterUserDto.getInvalidLoginAttempts());
		details.setLocale(filterUserDto.getLocale());
		details.setName(filterUserDto.getName());
		details.setUserCompanyName(filterUserDto.getUserCompanyName());
		details.setUpdatedAfter(filterUserDto.getUpdatedAfter());
		details.setEmailExactMatch(filterUserDto.isEmailExactMatch());
		return details;
	}
}
