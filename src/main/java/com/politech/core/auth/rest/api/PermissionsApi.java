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

@Tag(name = "Permissions", description = "This API offers functionality to retrieve system permissions")
@RequestMapping("/permissions")
public interface PermissionsApi
{

	@PreAuthorize("hasGlobalPermission('READ_USERS')")
	@Operation(summary = "List permissions", description = "Retrieves a list of all permissions in system", security = {
		@SecurityRequirement(name = SECURITY_REQUIREMENT) })
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<List<PermissionDto>> getAllPermissions();

}
