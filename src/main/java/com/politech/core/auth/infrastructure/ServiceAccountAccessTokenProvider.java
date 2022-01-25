package com.politech.core.auth.infrastructure;

import com.politech.core.auth.domain.model.users.User;
import com.politech.core.auth.domain.service.users.UserService;
import com.politech.core.auth.infrastructure.common.security.JwtTokenUtil;
import com.politech.core.auth.infrastructure.common.security.JwtUser;
import com.politech.core.auth.security.PermissionToAuthorityConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * This class obtains an access token for the service account. As this is the auth service itself
 * it doesn't really make sense to use the public endpoints, we're better off calling the methods
 * directly. Other services will of course need to use public endpoints. Because of this we
 * also don't need the password of the service account here.
 */
@Service
public class ServiceAccountAccessTokenProvider
{

	private final String serviceAccountEmail;

	private final JwtTokenUtil jwtTokenUtil;

	private final UserService userService;

	private String accessToken;

	public ServiceAccountAccessTokenProvider(final UserService userService, final JwtTokenUtil jwtTokenUtil,
		@Value("${internal.service-account.email}") final String serviceAccountEmail)
	{
		this.userService = userService;
		this.jwtTokenUtil = jwtTokenUtil;
		this.serviceAccountEmail = serviceAccountEmail;
	}

	public String getAccessToken()
	{
		if (!isAccessTokenValid(accessToken))
		{
			accessToken = refreshAccessToken();
		}
		return accessToken;
	}

	public boolean isAccessTokenValid(final String accessToken)
	{
		// TODO: We should have logic here which checks the expiration locally
		return false;
	}

	public String refreshAccessToken()
	{
		final User user = userService.findByEmail(serviceAccountEmail);
		return jwtTokenUtil.generateToken(createJwtUser(user));
	}

	private JwtUser createJwtUser(final User user)
	{
		return new JwtUser(user.getId(), user.getEmail(), user.getName(),
			PermissionToAuthorityConverter.convertPermissionsToGrantedAuthorities(user.getPermissions()));
	}
}
