package com.politech.core.auth.rest.endpoint;

import com.politech.core.auth.domain.model.token.*;
import com.politech.core.auth.domain.model.users.User;
import com.politech.core.auth.domain.service.system.SystemDateTimeService;
import com.politech.core.auth.domain.service.token.TokenService;
import com.politech.core.auth.domain.service.users.UserService;
import com.politech.core.auth.infrastructure.common.security.JwtTokenUtil;
import com.politech.core.auth.infrastructure.common.security.JwtUser;
import com.politech.core.auth.infrastructure.common.security.SecurityConstants;
import com.politech.core.auth.rest.api.AuthenticationApi;
import com.politech.core.auth.rest.dto.tokens.OneTimeLoginTokenRequestDto;
import com.politech.core.auth.rest.dto.tokens.OneTimeLoginTokenResponseDto;
import com.politech.core.auth.rest.dto.tokens.RefreshTokenDto;
import com.politech.core.auth.rest.dto.users.AccessTokenDto;
import com.politech.core.auth.rest.dto.users.LoginDto;
import com.politech.core.auth.security.PermissionToAuthorityConverter;
import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class AuthenticationController implements AuthenticationApi
{

	private static Logger LOG = LoggerFactory.getLogger(AuthenticationController.class);

	private static final String REQUEST_IP_ADDRESS = "X-FORWARDED-FOR";

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private TokenService tokenService;

	@Autowired
	private UserService userService;

	@Autowired
	private SystemDateTimeService systemDateTimeService;

	@Override
	public ResponseEntity<AccessTokenDto> generateToken(final LoginDto login, final boolean includeRefreshToken, final HttpHeaders requestHeaders)
	{

		LOG.debug("Generate token for {}", login.getEmail());
		final Authentication authentication;
		try
		{
			authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login.getEmail(), login.getPassword()));
		}
		catch (final BadCredentialsException ex)
		{
			LOG.debug("Invalid credentials, logging invalid login attempt");
			userService.recordInvalidLoginAttempt(login.getEmail(), requestHeaders.getFirst(HttpHeaders.USER_AGENT),
				requestHeaders.getFirst(REQUEST_IP_ADDRESS));
			throw ex;
		}
		LOG.debug("Login successfull");
		SecurityContextHolder.getContext().setAuthentication(authentication);
		final User user = userService.findByEmail(login.getEmail());
		final String token = jwtTokenUtil.generateToken(createJwtUser(user));
		final AccessTokenDto accessTokenDto = createAccessToken(user, includeRefreshToken, token);
		userService.updateLastLogin(user.getId(), systemDateTimeService.getCurrentDateTime());
		return ResponseEntity.ok(accessTokenDto);
	}

	private AccessTokenDto createAccessToken(final User user, final boolean includeRefreshToken, final String token)
	{
		final AccessTokenDto accessTokenDto;
		if (includeRefreshToken)
		{
			final Token refreshToken = tokenService.createRefreshToken(user.getId());
			accessTokenDto = createAccessTokenDto(token, refreshToken.getToken());
		}
		else
		{
			accessTokenDto = createAccessTokenDto(token, null);
		}
		return accessTokenDto;
	}

	@Override
	public ResponseEntity<AccessTokenDto> refreshToken(final RefreshTokenDto token)
	{
		LOG.debug("Login with refresh token '{}'", token);
		try
		{
			final Token refreshToken = tokenService.findByToken(token.getToken());
			if (refreshToken.getState() == TokenState.ISSUED)
			{
				// Invalidate old refresh token
				tokenService.useToken(refreshToken.getId());
				// Create new refresh token
				final Token newRefreshToken = tokenService.createRefreshToken(refreshToken.getUser().getId());
				// Generate new access token
				final User user = userService.findById(refreshToken.getUser().getId());
				final String accessToken = jwtTokenUtil.generateToken(createJwtUser(user));
				return ResponseEntity.ok(createAccessTokenDto(accessToken, newRefreshToken.getToken()));
			}
		}
		catch (final InvalidTokenException ex)
		{
			// Log & swallow, we'll throw an exception below anyway
			LOG.debug("Unable to find refresh token {}", token);
		}
		throw new BadCredentialsException(String.format("Refresh token '%s' is invalid, expired or revoked", token.getToken()));
	}

	@Override
	public ResponseEntity<Void> revokeToken(final RefreshTokenDto token, final JwtUser jwtUser)
	{
		try
		{
			final Token refreshToken = tokenService.findByToken(token.getToken());
			final boolean valid = refreshToken.getType() == TokenType.REFRESH && refreshToken.getUser().getId().equals(jwtUser.getId());
			if (valid)
			{
				revokeToken(refreshToken);
				return ResponseEntity.ok().build();
			}
		}
		catch (final InvalidTokenException ex)
		{
			// Swallow, we'll throw an exception below anyway
			LOG.debug("Unable to find refresh token {}", token);
		}
		throw new BadCredentialsException(
			String.format("Refresh token '%s' is invalid, expired or revoked (for the currently logged in user)", token.getToken()));
	}

	@Override
	public ResponseEntity<OneTimeLoginTokenResponseDto> generateOneTimeLoginToken()
	{
		final Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal != null && principal instanceof JwtUser)
		{
			final Long userId = ((JwtUser) principal).getId();
			final Token token = tokenService.createOneTimeLoginToken(userId);
			return ResponseEntity.ok(new OneTimeLoginTokenResponseDto(token.getToken(), tokenService.getOneTimeLoginTokenExpiryInSeconds()));
		}
		else
		{
			throw new NotImplementedException("Authentication mechanism not supported");
		}
	}

	@Override
	public ResponseEntity<AccessTokenDto> exchangeOneTimeLoginToken(@Valid OneTimeLoginTokenRequestDto dto)
	{
		LOG.debug("Login with auth code '{}'", dto);
		try
		{
			final OneTimeLoginToken token = getOneTimeLoginToken(dto.getToken());
			// Invalidate token
			tokenService.useToken(token.getId());
			// Create new refresh token
			final Token newRefreshToken = tokenService.createRefreshToken(token.getUser().getId());
			// Generate new access token
			final User user = userService.findById(token.getUser().getId());
			final String accessToken = jwtTokenUtil.generateToken(createJwtUser(user));
			return ResponseEntity.ok(createAccessTokenDto(accessToken, newRefreshToken.getToken()));
		}
		catch (final InvalidTokenException ex)
		{
			// Log & swallow, we'll throw an exception below anyway
			LOG.debug("Unable to find one-time login token {}", dto);
		}
		throw new BadCredentialsException(String.format("One-time login token '%s' is invalid or expired", dto.getToken()));
	}

	private void revokeToken(final Token token)
	{
		if (token.getState() == TokenState.ISSUED)
		{
			tokenService.revokeToken(token.getId());
		}
	}

	private AccessTokenDto createAccessTokenDto(final String accessToken, final String refreshToken)
	{
		return new AccessTokenDto(accessToken, SecurityConstants.ACCESS_TOKEN_VALIDITY_SECONDS, refreshToken);
	}

	private OneTimeLoginToken getOneTimeLoginToken(final String tokenValue)
	{
		final Token token = tokenService.findByToken(tokenValue);
		if (token.getType() == TokenType.ONE_TIME_LOGIN)
		{
			return (OneTimeLoginToken) token;
		}
		throw new InvalidTokenException("Given token is not a one-time login token");
	}

	private JwtUser createJwtUser(final User user)
	{
		return new JwtUser(user.getId(), user.getEmail(), user.getName(),
			PermissionToAuthorityConverter.convertPermissionsToGrantedAuthorities(user.getPermissions()));
	}
}
