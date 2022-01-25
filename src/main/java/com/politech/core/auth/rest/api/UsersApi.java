package com.politech.core.auth.rest.api;

import com.politech.core.auth.domain.model.users.Permission;
import com.politech.core.auth.infrastructure.common.presentation.rest.dto.PagedResultsDto;
import com.politech.core.auth.infrastructure.common.presentation.rest.dto.PaginationRequest;
import com.politech.core.auth.infrastructure.common.presentation.rest.dto.SortRequest;
import com.politech.core.auth.infrastructure.common.security.JwtUser;
import com.politech.core.auth.rest.dto.tokens.OneTimeLoginTokenResponseDto;
import com.politech.core.auth.rest.dto.users.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

import static com.politech.core.auth.infrastructure.common.docs.OpenApiConstants.SECURITY_REQUIREMENT;

@SuppressWarnings("PMD.AvoidDuplicateLiterals")
@Tag(name = "Users", description = "This API offers functionalities to create and retrieve user accounts.")
@RequestMapping("/users")
public interface UsersApi
{
	String IS_USER = "isUser(#id)";
	String READ_USERS = "hasGlobalPermission('READ_USERS')";
	String WRITE_USERS = "hasGlobalPermission('WRITE_USERS')";
	String READ_USERS_OR_IS_USER = READ_USERS + " or " + IS_USER;
	String WRITE_USERS_OR_IS_USER = WRITE_USERS + " or " + IS_USER;
	String IMPERSONATE_USERS = "hasGlobalPermission('IMPERSONATE_USERS')";

	@PreAuthorize(READ_USERS)
	@Operation(summary = "List users", description = "Retrieves a list of all users", security = {
		@SecurityRequirement(name = SECURITY_REQUIREMENT) })
	@Parameters(value = {
		@Parameter(in = ParameterIn.QUERY, name = "sort", description = "Format: (property;direction). Direction - asc,desc. Multiple sort criteria are supported.", array = @ArraySchema(schema = @Schema(allowableValues = {
			"name;asc", "name;desc", "email;asc", "email;desc" }, type = "string"))) })
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<PagedResultsDto<UserDto>> getAllUsers(@ParameterObject final GetAllUsersRequest userSearchDto,
														 @ParameterObject final PaginationRequest paginationRequest, @ParameterObject final SortRequest sortRequest);

	@Deprecated
	@PreAuthorize(READ_USERS)
	@Operation(summary = "Filter users by role", description = "[DEPRECATED] Retrieves a list of all users for specific role", security = {
		@SecurityRequirement(name = SECURITY_REQUIREMENT) })
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET, value = "/role/{role}")
	ResponseEntity<PagedResultsDto<SimpleUserDto>> getUsersByRole(
		@Parameter(name = "role", required = true, description = "The role to filter on") @PathVariable(name = "role") final Permission role,
		@ParameterObject final PaginationRequest paginationRequest);

	@PreAuthorize(READ_USERS)
	@Operation(summary = "Filter users by role", description = "Retrieves a list of all users for specific permission", security = {
		@SecurityRequirement(name = SECURITY_REQUIREMENT) })
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET, value = "/permission/{permission}")
	ResponseEntity<PagedResultsDto<SimpleUserDto>> getUsersByPermission(
		@Parameter(name = "permission", required = true, description = "The permission to filter on") @PathVariable(name = "permission") final Permission permission,
		@ParameterObject final PaginationRequest paginationRequest);

	@PreAuthorize(READ_USERS_OR_IS_USER)
	@Operation(summary = "Get user by id", description = "Retrieves a specific user by id", security = {
		@SecurityRequirement(name = SECURITY_REQUIREMENT) })
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET, value = "/{id}")
	ResponseEntity<UserDto> getUserById(@Parameter(name = "id", description = "The id of the user to retrieve") @PathVariable final Long id);

	@Operation(summary = "Get currently logged in user info", description = "Returns currently logged in user info", security = {
		@SecurityRequirement(name = SECURITY_REQUIREMENT) })
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET, value = "/me")
	ResponseEntity<UserDto> getCurrentlyLoggedInUser();

	@Deprecated
	@Operation(summary = "Get currently logged in user roles", description = "[DEPRECATED] Returns currently logged in user roles list", security = {
		@SecurityRequirement(name = SECURITY_REQUIREMENT) })
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET, value = "/me/roles")
	ResponseEntity<Set<String>> getCurrentlyLoggedInUserRoles(@Parameter(hidden = true) @AuthenticationPrincipal JwtUser jwtUser);

	@Operation(summary = "Get currently logged in user permissions", description = "Returns currently logged in user permission list", security = {
		@SecurityRequirement(name = SECURITY_REQUIREMENT) })
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET, value = "/me/permissions")
	ResponseEntity<Set<String>> getCurrentlyLoggedInUserPermissions(@Parameter(hidden = true) @AuthenticationPrincipal JwtUser jwtUser);

	@Operation(summary = "Get currently logged in user organizations", description = "Returns currently logged in user organizations list", security = {
		@SecurityRequirement(name = SECURITY_REQUIREMENT) })
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/me/organizations")
	ResponseEntity<Set<String>> getCurrentlyLoggedInUserOrganizations(@Parameter(hidden = true) @AuthenticationPrincipal JwtUser jwtUser);

	@PreAuthorize(WRITE_USERS)
	@Operation(summary = "Create user", description = "Creates a new user account", security = { @SecurityRequirement(name = SECURITY_REQUIREMENT) })
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	ResponseEntity<UserDto> createUser(@Valid @RequestBody final CreateUserDto dto);

	@PreAuthorize(WRITE_USERS_OR_IS_USER)
	@Operation(summary = "Update user", description = "Updates a user account", security = { @SecurityRequirement(name = SECURITY_REQUIREMENT) })
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.PUT, value = "/{id}")
	ResponseEntity<Void> updateUser(@Parameter(name = "id", description = "The id of the user to update") @PathVariable final Long id,
		@Valid @RequestBody final UpdateUserDto dto);

	@PreAuthorize(WRITE_USERS)
	@Operation(summary = "Deactivate user", description = "Deactivates a user account", security = {
		@SecurityRequirement(name = SECURITY_REQUIREMENT) })
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST, value = "/{id}/deactivate")
	ResponseEntity<Void> deactivateUser(@Parameter(name = "id", description = "The id of the user to deactivate") @PathVariable final Long id);

	@PreAuthorize(WRITE_USERS)
	@Operation(summary = "Lock user", description = "Locks a user account", security = { @SecurityRequirement(name = SECURITY_REQUIREMENT) })
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST, value = "/{id}/lock")
	ResponseEntity<Void> lockUser(@Parameter(name = "id", description = "The id of the user to lock") @PathVariable final Long id);

	@PreAuthorize(WRITE_USERS)
	@Operation(summary = "Reactivate user", description = "Reactivates a user account after it was locked or deactivated", security = {
		@SecurityRequirement(name = SECURITY_REQUIREMENT) })
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST, value = "/{id}/activate")
	ResponseEntity<Void> enableUser(@Parameter(name = "id", description = "The id of the user to reactivate") @PathVariable final Long id);

	@Deprecated
	@PreAuthorize(READ_USERS_OR_IS_USER)
	@Operation(summary = "Get user roles", description = "[DEPRECATED] Retrieves the granted roles (permissions) for a user", security = {
		@SecurityRequirement(name = SECURITY_REQUIREMENT) })
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET, value = "/{id}/roles")
	ResponseEntity<List<UserPermissionDto>> getRolesForUser(
		@Parameter(name = "id", description = "The id of the user for which to retrieve roles") @PathVariable final Long id);

	@PreAuthorize(READ_USERS_OR_IS_USER)
	@Operation(summary = "Get user roles", description = "Retrieves the granted permissions for a user", security = {
		@SecurityRequirement(name = SECURITY_REQUIREMENT) })
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET, value = "/{id}/permissions")
	ResponseEntity<List<UserPermissionDto>> getPermissionsForUser(
		@Parameter(name = "id", description = "The id of the user for which to retrieve roles") @PathVariable final Long id);

	@Deprecated
	@PreAuthorize(WRITE_USERS)
	@Operation(summary = "Grant user role", description = "[DEPRECATED] Grants a role to the given user", security = {
		@SecurityRequirement(name = SECURITY_REQUIREMENT) })
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST, value = "/{id}/roles")
	ResponseEntity<Void> addRoleToUser(
		@Parameter(name = "id", description = "The id of the user to which to add the role") @PathVariable final Long id,
		@RequestBody final GrantUserPermissionDto dto);

	@PreAuthorize(WRITE_USERS)
	@Operation(summary = "Grant user role", description = "Grants a role to the given user", security = {
		@SecurityRequirement(name = SECURITY_REQUIREMENT) })
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST, value = "/{id}/permissions")
	ResponseEntity<Void> addPermissionToUser(
		@Parameter(name = "id", description = "The id of the user to which to add the role") @PathVariable final Long id,
		@RequestBody final GrantUserPermissionDto dto);

	@Deprecated
	@PreAuthorize(WRITE_USERS)
	@Operation(summary = "Revoke user role", description = "[DEPRECATED] Revokes a role from the given user", security = {
		@SecurityRequirement(name = SECURITY_REQUIREMENT) })
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.DELETE, value = "/{id}/roles/{roleId}")
	ResponseEntity<Void> removeRolesFromUser(
		@Parameter(name = "id", description = "The id of the user from which to remove the role") @PathVariable final Long id,
		@Parameter(name = "roleId", description = "The id of the role to remove") @PathVariable final Long roleId);

	@PreAuthorize(WRITE_USERS)
	@Operation(summary = "Revoke user role", description = "Revokes a role from the given user", security = {
		@SecurityRequirement(name = SECURITY_REQUIREMENT) })
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.DELETE, value = "/{id}/permissions/{permissionId}")
	ResponseEntity<Void> removePermissionFromUser(
		@Parameter(name = "id", description = "The id of the user from which to remove the role") @PathVariable final Long id,
		@Parameter(name = "permissionId", description = "The id of the permission to remove") @PathVariable final Long permissionId);

	@PreAuthorize(WRITE_USERS)
	@Operation(summary = "Impersonation allowed", description = "Determines if the currently logged in user is allowed to impersonate the given user", security = {
		@SecurityRequirement(name = SECURITY_REQUIREMENT) })
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET, value = "/{id}/impersonation-allowed")
	ResponseEntity<ImpersonationAllowedDto> getImpersonationAllowed(
		@Parameter(name = "id", description = "The id of the user to impersonate") @PathVariable final Long id);

	@PreAuthorize(WRITE_USERS)
	@Operation(summary = "Impersonate user", description = "Creates a one-time login token to be able to impersonate the user", security = {
		@SecurityRequirement(name = SECURITY_REQUIREMENT) })
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST, value = "/{id}/impersonate")
	ResponseEntity<OneTimeLoginTokenResponseDto> impersonate(
		@Parameter(name = "id", description = "The id of the user to impersonate") @PathVariable final Long id);
}
