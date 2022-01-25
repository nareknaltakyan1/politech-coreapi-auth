package com.politech.core.auth.rest.endpoint;

import com.politech.core.auth.domain.model.token.Token;
import com.politech.core.auth.domain.model.users.*;
import com.politech.core.auth.domain.service.token.TokenService;
import com.politech.core.auth.domain.service.users.UserService;
import com.politech.core.auth.infrastructure.common.presentation.rest.dto.PagedResultsDto;
import com.politech.core.auth.infrastructure.common.presentation.rest.dto.PaginationRequest;
import com.politech.core.auth.infrastructure.common.presentation.rest.dto.SortRequest;
import com.politech.core.auth.infrastructure.common.presentation.rest.dto.SortSpecification;
import com.politech.core.auth.infrastructure.common.security.JwtUser;
import com.politech.core.auth.infrastructure.common.validation.PasswordPatternValidator;
import com.politech.core.auth.infrastructure.common.validation.UniqueConstraintException;
import com.politech.core.auth.rest.api.UsersApi;
import com.politech.core.auth.rest.dto.tokens.OneTimeLoginTokenResponseDto;
import com.politech.core.auth.rest.dto.users.*;
import com.politech.core.auth.rest.exception.AuthErrorField;
import com.politech.core.auth.rest.exception.InvalidPasswordFormatException;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.util.Assert.notNull;

@RestController
public class UserController implements UsersApi
{
	@Autowired
	private UserService userService;

	@Autowired
	private TokenService tokenService;

	@Autowired
	private PasswordPatternValidator passwordPatternValidator;

	@Autowired
	private UserDtoConverter userDtoConverter;

	@Autowired
	private OrganizationDtoConverter organizationDtoConverter;

	@Autowired
	private GetAllUsersDetailsConverter getAllUsersDetailsConverter;

	@Override
	public ResponseEntity<PagedResultsDto<UserDto>> getAllUsers(final GetAllUsersRequest filterUserDto, final PaginationRequest paginationRequest,
																final SortRequest sortRequest)
	{
		final PageRequest pageRequest = createPageRequest(paginationRequest, sortRequest);
		final GetAllUsersDetails getAllUsersPojo = getAllUsersDetailsConverter.convertToDetails(filterUserDto);
		final Page<UserDto> userDtos = userService.findAll(getAllUsersPojo, pageRequest).map(userDtoConverter::convertToDto);
		return ResponseEntity.ok(new PagedResultsDto<>(userDtos));
	}

	@Override
	public ResponseEntity<PagedResultsDto<SimpleUserDto>> getUsersByPermission(final Permission role, final PaginationRequest paginationRequest)
	{
		var userDtos = userService.findAllByPermission(role, PageRequest.of(paginationRequest.getPage(), paginationRequest.getSize()))
			.map(this::toDto);
		return ResponseEntity.ok(new PagedResultsDto<>(userDtos));
	}

	@Override
	public ResponseEntity<UserDto> getUserById(final Long id)
	{
		try
		{
			return ResponseEntity.ok(userDtoConverter.convertToDto(userService.findById(id)));
		}
		catch (final EntityNotFoundException ex)
		{
			return ResponseEntity.notFound().build();
		}
	}

	@Override
	public ResponseEntity<UserDto> getCurrentlyLoggedInUser()
	{
		final Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof JwtUser)
		{
			final String userName = ((JwtUser) principal).getEmail();
			return ResponseEntity.ok(userDtoConverter.convertToDto(userService.findByEmail(userName)));
		}
		else
		{
			throw new NotImplementedException("Authentication mechanism not supported");
		}
	}

	@Override
	public ResponseEntity<Set<String>> getCurrentlyLoggedInUserPermissions(final JwtUser jwtUser)
	{
		final Set<String> authorities = jwtUser.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
		return ResponseEntity.ok(authorities);
	}

	@Override
	public ResponseEntity<Set<String>> getCurrentlyLoggedInUserOrganizations(final JwtUser jwtUser)
	{
		final var organizationIds = jwtUser.getAuthorities().stream().map(GrantedAuthority::getAuthority)
			.filter(authority -> authority.startsWith("ORG")).map(authority -> authority.substring(authority.indexOf(":") + 1))
			.collect(Collectors.toSet());
		return ResponseEntity.ok(organizationIds);
	}

	@Override
	public ResponseEntity<UserDto> createUser(final CreateUserDto dto)
	{
		validatePasswordIfNeeded(dto);
		if (Objects.nonNull(dto.getEmail()) && userService.userExistsByEmail(dto.getEmail()))
		{
			throw new UniqueConstraintException(AuthErrorField.EMAIL.getCode());
		}
		if (Objects.nonNull(dto.getPhoneNumber()) && userService.userExistsByPhoneNumber(dto.getPhoneNumber()))
		{
			throw new UniqueConstraintException(AuthErrorField.PHONE_NUMBER.getCode());
		}
		final User user = userService.createUser(toUserDetails(dto));
		return ResponseEntity.created(URI.create(String.format("./%d", user.getId()))).body(userDtoConverter.convertToDto(user));
	}

	@Override
	public ResponseEntity<Void> updateUser(final Long id, final UpdateUserDto dto)
	{
		userService.updateUser(id, toUserDetails(dto));
		return ResponseEntity.noContent().build();
	}

	@Override
	public ResponseEntity<Void> deactivateUser(final Long id)
	{
		userService.updateAccountState(id, UserAccountState.DEACTIVATED);
		return ResponseEntity.noContent().build();
	}

	@Override
	public ResponseEntity<Void> lockUser(final Long id)
	{
		userService.updateAccountState(id, UserAccountState.LOCKED);
		return ResponseEntity.noContent().build();
	}

	@Override
	public ResponseEntity<Void> enableUser(final Long id)
	{
		userService.updateAccountState(id, UserAccountState.ACTIVE);
		return ResponseEntity.noContent().build();
	}

	@Override
	public ResponseEntity<List<UserPermissionDto>> getPermissionsForUser(final Long id)
	{
		final User user = userService.findById(id);
		final List<UserPermissionDto> roles = new ArrayList<>();
		for (final UserPermission role : user.getPermissions())
		{
			final UserPermissionDto dto = new UserPermissionDto();
			dto.setId(role.getId());
			dto.setPermission(role.getPermission());
			if (role.getType() == PermissionType.ORGANIZATIONAL)
			{
				dto.setOrganization(organizationDtoConverter.convertToDto(((OrganizationalUserPermission) role).getOrganization()));
			}
			roles.add(dto);
		}
		return ResponseEntity.ok(roles);
	}

	@Override
	public ResponseEntity<Void> addPermissionToUser(final Long id, final GrantUserPermissionDto dto)
	{
		boolean updated = processGlobalGrant(id, dto);
		if (!updated)
		{
			updated = processOrganizationalGrant(id, dto);
		}
		if (updated)
		{
			return ResponseEntity.noContent().build();
		}
		else
		{
			throw new NotImplementedException(String.format("Role type '%s' is not supported", dto.getPermission().getType()));
		}
	}

	private boolean processGlobalGrant(final Long id, final GrantUserPermissionDto dto)
	{
		if (dto.getPermission().getType() == PermissionType.GLOBAL)
		{
			userService.grantGlobalRoleToUser(id, dto.getPermission());
			return true;
		}
		return false;
	}

	private boolean processOrganizationalGrant(final Long id, final GrantUserPermissionDto dto)
	{
		if (dto.getPermission().getType() == PermissionType.ORGANIZATIONAL)
		{
			notNull(dto.getOrganizationId(), String.format(
				"Permission '%s' can only granted in the context of an organization, but no organization id was supplied", dto.getPermission()));
			userService.grantOrganizationalPermissionToUser(id, dto.getOrganizationId(), dto.getPermission());
			return true;
		}
		return false;
	}

	@Override
	public ResponseEntity<Void> removePermissionFromUser(final Long id, final Long roleId)
	{
		userService.revokePermissionFromUser(id, roleId);
		return ResponseEntity.noContent().build();
	}

	@Override
	public ResponseEntity<ImpersonationAllowedDto> getImpersonationAllowed(final Long id)
	{
		notNull(id, "User id may not be null");
		return ResponseEntity.ok(new ImpersonationAllowedDto(isImpersonationAllowed(id)));
	}

	@Override
	public ResponseEntity<OneTimeLoginTokenResponseDto> impersonate(final Long id)
	{
		notNull(id, "User id may not be null");
		if (!isImpersonationAllowed(id))
		{
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		final Token token = tokenService.createOneTimeLoginToken(id);
		return ResponseEntity.ok(new OneTimeLoginTokenResponseDto(token.getToken(), tokenService.getOneTimeLoginTokenExpiryInSeconds()));
	}

	private boolean isImpersonationAllowed(final Long id)
	{
		final User user = userService.findById(id);
		return user.getPermissions().stream().filter(
			userPermission -> (userPermission.getType() == PermissionType.GLOBAL && userPermission.getPermission() != Permission.READ_NON_SENSITIVE))
			.findFirst().isEmpty();
	}

	@Deprecated
	@Override
	public ResponseEntity<PagedResultsDto<SimpleUserDto>> getUsersByRole(Permission role, PaginationRequest paginationRequest)
	{
		return getUsersByPermission(role, paginationRequest);
	}

	@Deprecated
	@Override
	public ResponseEntity<Set<String>> getCurrentlyLoggedInUserRoles(JwtUser jwtUser)
	{
		return getCurrentlyLoggedInUserPermissions(jwtUser);
	}

	@Deprecated
	@Override
	public ResponseEntity<List<UserPermissionDto>> getRolesForUser(Long id)
	{
		return getPermissionsForUser(id);
	}

	@Deprecated
	@Override
	public ResponseEntity<Void> addRoleToUser(Long id, GrantUserPermissionDto dto)
	{
		return addPermissionToUser(id, dto);
	}

	@Deprecated
	@Override
	public ResponseEntity<Void> removeRolesFromUser(Long id, Long roleId)
	{
		return removePermissionFromUser(id, roleId);
	}

	private CreateUserDetails toUserDetails(final CreateUserDto dto)
	{
		final Locale locale = Optional.ofNullable(dto.getLocale()).orElse(Locale.UK);
		return new CreateUserDetailsBuilder().withName(dto.getName()).withEmail(dto.getEmail()).withPhoneNumber(dto.getPhoneNumber())
			.withPassword(dto.getPassword()).withGeneratePassword(dto.getGeneratePassword()).withLocale(locale).withWaitForSync(dto.getWaitForSync())
			.withSendEmail(dto.getSendEmail()).build();
	}

	private UpdateUserDetails toUserDetails(final UpdateUserDto dto)
	{
		final Locale locale = Optional.ofNullable(dto.getLocale()).orElse(Locale.UK);
		return new UpdateUserDetailsBuilder().withName(dto.getName()).withEmail(dto.getEmail()).withPhoneNumber(dto.getPhoneNumber())
			.withLocale(locale).withWaitForSync(dto.getWaitForSync()).build();
	}

	private void validatePasswordIfNeeded(final CreateUserDto dto)
	{
		if (Boolean.TRUE.equals(dto.getGeneratePassword()))
		{
			return;
		}
		if (!passwordPatternValidator.isValid(dto.getPassword(), null))
		{
			throw new InvalidPasswordFormatException(AuthErrorField.PASSWORD.getCode());
		}
	}

	private PageRequest createPageRequest(final PaginationRequest paginationRequest, final SortRequest sortRequest)
	{
		final Sort sort = SortSpecification.toSort(sortRequest.getSort(), UserSortSpecification.values());
		return PageRequest.of(paginationRequest.getPage(), paginationRequest.getSize(), sort);
	}

	private SimpleUserDto toDto(final User user)
	{
		return new SimpleUserDto(user.getId(), user.getName());
	}
}
