package com.politech.core.auth.rest.api;

import com.politech.core.auth.rest.dto.users.PermissionDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import static com.politech.core.auth.infrastructure.common.docs.OpenApiConstants.SECURITY_REQUIREMENT;

@Deprecated
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
@Tag(name = "Roles", description = "This API offers functionality to retrieve system roles")
@RequestMapping("/roles")
public interface RolesApi
{

	@PreAuthorize("hasGlobalPermission('READ_USERS')")
	@Operation(summary = "List roles", description = "Retrieves a list of all roles in system", security = {
		@SecurityRequirement(name = SECURITY_REQUIREMENT) })
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<List<PermissionDto>> getAllPermissions();

}
