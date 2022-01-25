package com.politech.core.auth.rest.endpoint;

import com.politech.core.auth.domain.model.organizations.Organization;
import com.politech.core.auth.domain.model.users.GetAllUsersDetails;
import com.politech.core.auth.domain.service.organizations.OrganizationService;
import com.politech.core.auth.domain.service.users.UserService;
import com.politech.core.auth.infrastructure.common.presentation.rest.dto.PagedResultsDto;
import com.politech.core.auth.infrastructure.common.presentation.rest.dto.PaginationRequest;
import com.politech.core.auth.infrastructure.common.presentation.rest.dto.SortRequest;
import com.politech.core.auth.infrastructure.common.presentation.rest.dto.SortSpecification;
import com.politech.core.auth.rest.api.OrganizationsApi;
import com.politech.core.auth.rest.dto.organizations.CreateOrUpdateOrganizationDto;
import com.politech.core.auth.rest.dto.organizations.GetAllOrganizationMembersRequestDto;
import com.politech.core.auth.rest.dto.organizations.GetAllOrganizationsRequestDto;
import com.politech.core.auth.rest.dto.organizations.OrganizationDto;
import com.politech.core.auth.rest.dto.users.UserDto;
import com.politech.core.auth.rest.dto.users.UserSortSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import java.net.URI;

@RestController
public class OrganizationsController implements OrganizationsApi
{
	@Autowired
	private OrganizationService organizationService;

	@Autowired
	private UserService userService;

	@Autowired
	private UserDtoConverter userDtoConverter;

	@Autowired
	private OrganizationDtoConverter organizationDtoConverter;

	@Autowired
	private GetAllOrganizationsDetailsConverter getAllOrganizationsDetailsConverter;

	@Autowired
	private GetAllUsersDetailsConverter getAllUsersDetailsConverter;

	@Autowired
	private MutateOrganizationDetailsConverter mutateOrganizationDetailsConverter;

	@Override
	public ResponseEntity<PagedResultsDto<OrganizationDto>> getAllOrganizations(final Pageable pageable,
																				final GetAllOrganizationsRequestDto getAllOrganizationsRequest)
	{
		final Page<OrganizationDto> userDtos = organizationService
			.findAll(getAllOrganizationsDetailsConverter.convertToDetails(getAllOrganizationsRequest), pageable)
			.map(organizationDtoConverter::convertToDto);
		return ResponseEntity.ok(new PagedResultsDto<>(userDtos));
	}

	@Override
	public ResponseEntity<OrganizationDto> getOrganizationById(final Long id)
	{
		try
		{
			return ResponseEntity.ok(organizationDtoConverter.convertToDto(organizationService.findById(id)));
		}
		catch (final EntityNotFoundException ex)
		{
			return ResponseEntity.notFound().build();
		}
	}

	@Override
	public ResponseEntity<OrganizationDto> createOrganization(final CreateOrUpdateOrganizationDto dto)
	{
		final Organization org = organizationService.createOrganization(mutateOrganizationDetailsConverter.convert(dto));
		return ResponseEntity.created(URI.create(String.format("./%d", org.getId()))).body(organizationDtoConverter.convertToDto(org));
	}

	@Override
	public ResponseEntity<Void> updateOrganization(final Long id, final CreateOrUpdateOrganizationDto dto)
	{
		organizationService.updateOrganization(id, mutateOrganizationDetailsConverter.convert(dto));
		return ResponseEntity.noContent().build();
	}

	@Override
	public ResponseEntity<PagedResultsDto<UserDto>> listOrganizationMembers(final Long id, final GetAllOrganizationMembersRequestDto membersRequest,
																			final PaginationRequest paginationRequest, final SortRequest sortRequest)
	{
		final PageRequest pageRequest = createPageRequest(paginationRequest, sortRequest);
		final GetAllUsersDetails getAllUsersPojo = getAllUsersDetailsConverter.convertToDetails(id, membersRequest);
		final Page<UserDto> members = userService.findAll(getAllUsersPojo, pageRequest).map(userDtoConverter::convertToDto);
		return ResponseEntity.ok(new PagedResultsDto<>(members));
	}

	private PageRequest createPageRequest(final PaginationRequest paginationRequest, final SortRequest sortRequest)
	{
		final Sort sort = SortSpecification.toSort(sortRequest.getSort(), UserSortSpecification.values());
		return PageRequest.of(paginationRequest.getPage(), paginationRequest.getSize(), sort);
	}
}
