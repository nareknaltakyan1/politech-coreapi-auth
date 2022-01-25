package com.politech.core.auth.rest.api;

import com.politech.core.auth.infrastructure.common.security.JwtUser;
import com.politech.core.auth.rest.dto.tokens.OneTimeLoginTokenRequestDto;
import com.politech.core.auth.rest.dto.tokens.OneTimeLoginTokenResponseDto;
import com.politech.core.auth.rest.dto.tokens.RefreshTokenDto;
import com.politech.core.auth.rest.dto.users.AccessTokenDto;
import com.politech.core.auth.rest.dto.users.LoginDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.politech.core.auth.infrastructure.common.docs.OpenApiConstants.SECURITY_REQUIREMENT;


@SuppressWarnings("PMD.AvoidDuplicateLiterals")
@Tag(name = "Authentication", description = "The authentication API allows users to authenticate and obtain a JWT token.")
@RequestMapping("/auth")
public interface AuthenticationApi
{
	@Operation(summary = "Generate access token with username & password", description = "Generates an access token based on a username & password to perform authenticated requests")
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = "/generate-token")
	ResponseEntity<AccessTokenDto> generateToken(@Valid @RequestBody final LoginDto login,
												 @RequestParam(name = "refreshToken", required = false) final boolean includeRefreshToken, @RequestHeader final HttpHeaders requestHeaders);

	@Operation(summary = "Generate access token with refresh token", description = "Generates an access token based on a refresh token to perform authenticated requests")
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = "/refresh-token")
	ResponseEntity<AccessTokenDto> refreshToken(@Valid @RequestBody final RefreshTokenDto token);

	@Operation(summary = "Revokes a refresh token", description = "Revokes a refresh token so that the token can no longer be used for obtaining an access token", security = {
		@SecurityRequirement(name = SECURITY_REQUIREMENT) })
	@DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = "/refresh-token")
	ResponseEntity<Void> revokeToken(@Valid @RequestBody final RefreshTokenDto token,
		@Parameter(hidden = true) @AuthenticationPrincipal final JwtUser jwtUser);

	@Operation(summary = "Generate one-time login token", description = "Generates a short-lived one-time login token for the currently logged in user", security = {
		@SecurityRequirement(name = SECURITY_REQUIREMENT) })
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = "/one-time-login-token/generate")
	ResponseEntity<OneTimeLoginTokenResponseDto> generateOneTimeLoginToken();

	@Operation(summary = "Generate access token with one-time login token", description = "Generates an access token based on a one-time login token to perform authenticated requests")
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = "/one-time-login-token")
	ResponseEntity<AccessTokenDto> exchangeOneTimeLoginToken(@Valid @RequestBody final OneTimeLoginTokenRequestDto token);
}
