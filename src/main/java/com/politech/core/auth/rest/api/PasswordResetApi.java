package com.politech.core.auth.rest.api;

import com.politech.core.auth.rest.dto.users.PasswordResetDto;
import com.politech.core.auth.rest.dto.users.RenewDynamicPasswordDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@SuppressWarnings("PMD.AvoidDuplicateLiterals")
@Tag(name = "Password Reset", description = "This API allows users to reset the password for a specific account or to request a password reset e-mail.")
@RequestMapping("/password-reset")
public interface PasswordResetApi
{
	// TODO add phone number for password reset
	@Operation(summary = "Request password reset", description = "Requests a password reset token to be sent to the user's email address")
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.TEXT_PLAIN_VALUE, method = RequestMethod.POST, path = "/request-token")
	void initiatePasswordReset(@RequestBody final String email);

	@Operation(summary = "Password reset", description = "Performs the password reset with the provided password reset token")
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	void performPasswordReset(@Valid @RequestBody final PasswordResetDto dto);

	@Operation(summary = "Renew dynamic password", description = "Generates a new dynamic password for the provided user and sends it through the relevant channel")
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = "/renew-dynamic-password")
	ResponseEntity<Void> renewDynamicPassword(@Valid @RequestBody final RenewDynamicPasswordDto dto);
}
