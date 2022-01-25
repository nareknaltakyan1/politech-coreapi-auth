package com.politech.core.auth.rest.endpoint;

import com.politech.core.auth.domain.model.users.Permission;
import com.politech.core.auth.rest.api.RolesApi;
import com.politech.core.auth.rest.dto.users.PermissionDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Deprecated
@RestController
public class RolesController implements RolesApi
{

	@Override
	public ResponseEntity<List<PermissionDto>> getAllPermissions()
	{
		final List<PermissionDto> roleDtos = Arrays.stream(Permission.values()).map(PermissionDto::new).collect(Collectors.toList());
		return ResponseEntity.ok(roleDtos);
	}
}
