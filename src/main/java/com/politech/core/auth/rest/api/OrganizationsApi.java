package com.politech.core.auth.rest.api;

import com.politech.core.auth.infrastructure.common.presentation.rest.dto.PagedResultsDto;
import com.politech.core.auth.infrastructure.common.presentation.rest.dto.PaginationRequest;
import com.politech.core.auth.infrastructure.common.presentation.rest.dto.SortRequest;
import com.politech.core.auth.rest.dto.organizations.CreateOrUpdateOrganizationDto;
import com.politech.core.auth.rest.dto.organizations.GetAllOrganizationMembersRequestDto;
import com.politech.core.auth.rest.dto.organizations.GetAllOrganizationsRequestDto;
import com.politech.core.auth.rest.dto.organizations.OrganizationDto;
import com.politech.core.auth.rest.dto.users.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

import static com.politech.core.auth.infrastructure.common.docs.OpenApiConstants.SECURITY_REQUIREMENT;

@SuppressWarnings("PMD.AvoidDuplicateLiterals")
@Tag(name = "Organizations", description = "This API offers functionalities to create and retrieve organizations and it's members.")
@RequestMapping("/organizations")
public interface OrganizationsApi
{
	String IS_MEMBER = "isOrgMember(#id)";
	String READ_ORGS = "hasGlobalPermission('READ_ORGS')";
	String WRITE_ORGS = "hasGlobalPermission('WRITE_ORGS')";
	String READ_ORGS_OR_IS_MEMBER = READ_ORGS + " or " + IS_MEMBER;
	String WRITE_ORGS_OR_IS_MEMBER = WRITE_ORGS + " or " + IS_MEMBER;

	@PreAuthorize(READ_ORGS)
	@Operation(summary = "List organizations", description = "Retrieves a list of all organizations", security = {
		@SecurityRequirement(name = SECURITY_REQUIREMENT) })
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	ResponseEntity<PagedResultsDto<OrganizationDto>> getAllOrganizations(@ParameterObject final Pageable pageable,
																		 @ParameterObject final GetAllOrganizationsRequestDto filterOrganizationDto);

	@PreAuthorize(READ_ORGS_OR_IS_MEMBER)
	@Operation(summary = "Get organization by id", description = "Retrieves a specific organization by id", security = {
		@SecurityRequirement(name = SECURITY_REQUIREMENT) })
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET, value = "/{id}")
	ResponseEntity<OrganizationDto> getOrganizationById(
		@Parameter(name = "id", description = "The id of the organization to retrieve") @PathVariable final Long id);

	@PreAuthorize(WRITE_ORGS)
	@Operation(summary = "Create organization", description = "Creates a new organization", security = {
		@SecurityRequirement(name = SECURITY_REQUIREMENT) })
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	ResponseEntity<OrganizationDto> createOrganization(@Valid @RequestBody final CreateOrUpdateOrganizationDto dto);

	@PreAuthorize(WRITE_ORGS)
	@Operation(summary = "Update organization", description = "Creates a new organization", security = {
		@SecurityRequirement(name = SECURITY_REQUIREMENT) })
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.PUT, value = "/{id}")
	ResponseEntity<Void> updateOrganization(@Parameter(name = "id", description = "The id of the organization to update") @PathVariable final Long id,
		@RequestBody final CreateOrUpdateOrganizationDto dto);

	@PreAuthorize(READ_ORGS_OR_IS_MEMBER)
	@Operation(summary = "List organization members", description = "Retrieves a list of users that are member of the organization", security = {
		@SecurityRequirement(name = SECURITY_REQUIREMENT) })
	@Parameters(value = { @Parameter(in = ParameterIn.QUERY, name = "page", required = true),
		@Parameter(in = ParameterIn.QUERY, name = "size", required = true),
		@Parameter(in = ParameterIn.QUERY, name = "sort", description = "Format: (property;direction). Direction - asc,desc. Multiple sort criteria are supported.", array = @ArraySchema(schema = @Schema(allowableValues = {
			"id;asc", "id;desc", "name;asc", "name;desc", "email;asc", "email;desc" }, type = "string"))) })
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET, value = "/{id}/members")
	ResponseEntity<PagedResultsDto<UserDto>> listOrganizationMembers(
			@Parameter(name = "id", description = "The id of the organization for which to retrieve members") @PathVariable final Long id,
			@ParameterObject final GetAllOrganizationMembersRequestDto membersRequest, @ParameterObject final PaginationRequest paginationRequest,
			@ParameterObject final SortRequest sortRequest);
}
