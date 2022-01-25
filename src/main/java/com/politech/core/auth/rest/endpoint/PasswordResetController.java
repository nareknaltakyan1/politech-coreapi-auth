package com.politech.core.auth.rest.endpoint;

import com.google.gson.JsonObject;
import com.politech.core.auth.domain.model.token.InvalidTokenException;
import com.politech.core.auth.domain.model.token.PasswordResetToken;
import com.politech.core.auth.domain.model.token.Token;
import com.politech.core.auth.domain.model.token.TokenType;
import com.politech.core.auth.domain.model.users.PasswordMismatchException;
import com.politech.core.auth.domain.model.users.User;
import com.politech.core.auth.domain.service.token.TokenService;
import com.politech.core.auth.domain.service.users.UserService;
import com.politech.core.auth.rest.api.PasswordResetApi;
import com.politech.core.auth.rest.dto.users.PasswordResetDto;
import com.politech.core.auth.rest.dto.users.RenewDynamicPasswordDto;
import com.politech.core.auth.rest.exception.AuthErrorField;
import com.politech.core.auth.security.PasswordBasedKeyPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;


import static org.springframework.util.Assert.notNull;

@RestController
public class PasswordResetController implements PasswordResetApi
{

	@Autowired
	private TokenService tokenService;

	@Autowired
	private UserService userService;

	@Autowired
	private PasswordBasedKeyPasswordEncoder passwordEncoder;

//	@Autowired
//	private CommunicationService communicationService;

	@Value("${internal.from-name}")
	private String fromName;

	@Value("${internal.from-email}")
	private String fromEmail;

	@Override
	public void initiatePasswordReset(final String email)
	{
		notNull(email, "Email should not be null");
		if (userService.userExistsByEmail(email))
		{
			final User user = userService.findByEmail(email);
			PasswordResetToken resetToken = tokenService.createPasswordResetToken(user.getId());
			JsonObject data = new JsonObject();
			data.addProperty("name", user.getName());
			data.addProperty("token", resetToken.getToken());
			data.addProperty("expiresAt", resetToken.getExpiresAt().toString());
//			communicationService.sendEmail(EmailType.PASSWORD_RESET.name(), user.getLocale(), new EmailRecipient(fromName, fromEmail),
//				new EmailRecipient(user.getName(), user.getEmail()), data.toString());
		}
	}

	@Override
	public void performPasswordReset(final PasswordResetDto dto)
	{
		final User user = userService.findByEmail(dto.getEmail());
		if (StringUtils.hasText(dto.getOldPassword()))
		{
			// Use the old password to perform the password reset
			if (!samePasswords(dto.getOldPassword(), user.getSaltedPasswordHash()))
			{
				throw new PasswordMismatchException(AuthErrorField.OLD_PASSWORD.getCode());
			}
		}
		else
		{
			// Use the password reset token to perform the password reset
			verifyAndUsePasswordResetToken(dto.getPasswordResetToken());
		}
		// If we get here the request was valid and we can now update the password
		userService.changePassword(user.getId(), dto.getNewPassword());
		tokenService.revokeAllRefreshAndOneTimeLoginTokens(user.getId());
	}

	@Override
	public ResponseEntity<Void> renewDynamicPassword(final RenewDynamicPasswordDto dto)
	{
		// TODO Generate a new dynamic password of 6 numeric digits, store on user and send an SMS or email
		return ResponseEntity.noContent().build();
	}

	private void verifyAndUsePasswordResetToken(final String tokenValue)
	{
		final Token token = tokenService.findByToken(tokenValue, false);
		try
		{
			Assert.isTrue(token.getType() == TokenType.PASSWORD_RESET, "Token must be for password reset");
			tokenService.useToken(token.getId());
		}
		catch (Exception ex)
		{
			throw new InvalidTokenException("Token was incorrect, expired or revoked", ex);
		}
	}

	private boolean samePasswords(final String rawPassword, final String encodedPassword)
	{
		return passwordEncoder.matches(rawPassword, encodedPassword);
	}
}
