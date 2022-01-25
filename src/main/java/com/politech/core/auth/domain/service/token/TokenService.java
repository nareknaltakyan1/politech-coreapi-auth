package com.politech.core.auth.domain.service.token;

import com.politech.core.auth.domain.model.token.*;
import com.politech.core.auth.domain.model.users.User;
import com.politech.core.auth.domain.service.system.SystemDateTimeService;
import com.politech.core.auth.domain.service.users.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TokenService
{
	// O and 0 are omitted on purpose
	private static final String PASSWORD_RESET_TOKEN_CHARS = "ABCDEFGHIJKLMNPQRSTUVWXYZ123456789";
	private static final int PASSWORD_RESET_TOKEN_LENGTH = 6;
	private static final int DEFAULT_TOKEN_LENGTH = 36;

	@Autowired
	private TokenRepository repository;

	@Autowired
	private UserService userService;

	@Autowired
	private SystemDateTimeService systemDateTimeService;

	@Value("${internal.one-time-login-token-expiry-seconds}")
	private int oneTimeLoginTokenExpiryInSeconds;

	@Value("${internal.password-reset-token-expiry-seconds}")
	private int passwordResetTokenExpiryInSeconds;

	@Transactional(readOnly = true)
	public Token findById(final Long id)
	{
		final Optional<Token> refreshToken = repository.findById(id);
		if (refreshToken.isEmpty())
		{
			throw new EntityNotFoundException(String.format("Could not find refresh token with id '%d'", id));
		}
		return refreshToken.get();
	}

	@Transactional(readOnly = true)
	public Token findByToken(final String token)
	{
		return findByToken(token, true);
	}

	@Transactional(readOnly = true)
	public Token findByToken(final String token, boolean caseSensitive)
	{
		final Optional<Token> refreshToken;
		if (caseSensitive)
		{
			refreshToken = repository.findByToken(token);
		}
		else
		{
			refreshToken = repository.findByTokenIgnoreCase(token);
		}
		if (refreshToken.isEmpty())
		{
			throw new InvalidTokenException(String.format("Could not find refresh token with token '%s'", token));
		}
		return refreshToken.get();
	}

	@Transactional
	public RefreshToken createRefreshToken(final Long userId)
	{
		final User user = userService.findById(userId);
		RefreshToken token = new RefreshToken();
		token.setUser(user);
		token.setState(TokenState.ISSUED);
		token.setToken(generateTokenCode(DEFAULT_TOKEN_LENGTH));
		token.setCreated(systemDateTimeService.getCurrentDateTime());
		token = repository.save(token);
		return token;
	}

	@Transactional
	public PasswordResetToken createPasswordResetToken(final Long userId)
	{
		final User user = userService.findById(userId);
		revokeAllPasswordResetTokens(user);
		PasswordResetToken token = new PasswordResetToken();
		token.setUser(user);
		token.setState(TokenState.ISSUED);
		token.setToken(generateTokenCode(PASSWORD_RESET_TOKEN_LENGTH, PASSWORD_RESET_TOKEN_CHARS));
		token.setExpiresAt(systemDateTimeService.getCurrentDateTime().plusSeconds(getPasswordResetTokenExpiryInSeconds()));
		token.setCreated(systemDateTimeService.getCurrentDateTime());
		token = repository.save(token);
		return token;
	}

	@Transactional
	public OneTimeLoginToken createOneTimeLoginToken(final Long userId)
	{
		final User user = userService.findById(userId);
		OneTimeLoginToken token = new OneTimeLoginToken();
		token.setUser(user);
		token.setState(TokenState.ISSUED);
		token.setToken(generateTokenCode(DEFAULT_TOKEN_LENGTH));
		token.setExpiresAt(systemDateTimeService.getCurrentDateTime().plusSeconds(getOneTimeLoginTokenExpiryInSeconds()));
		token.setCreated(systemDateTimeService.getCurrentDateTime());
		token = repository.save(token);
		return token;
	}

	private void revokeAllPasswordResetTokens(final User user)
	{
		final List<Token> tokens = repository.findByUserAndState(user, TokenState.ISSUED);
		for (final Token token : tokens)
		{
			if (token.getType() == TokenType.PASSWORD_RESET)
			{
				updateState(token.getId(), TokenState.REVOKED);
			}
		}
	}

	@Transactional
	public void revokeAllRefreshAndOneTimeLoginTokens(final Long userId)
	{
		final User user = userService.findById(userId);
		final List<Token> tokens = repository.findByUserAndState(user, TokenState.ISSUED);
		for (final Token token : tokens)
		{
			if (token.getType() == TokenType.REFRESH || token.getType() == TokenType.ONE_TIME_LOGIN)
			{
				updateState(token.getId(), TokenState.REVOKED);
			}
		}
	}

	@Transactional
	public void useToken(final Long tokenId)
	{
		final Token t = repository.findByIdWithPessimisticLock(tokenId).get();
		if (t.getExpiresAt() != null && t.getExpiresAt().isBefore(systemDateTimeService.getCurrentDateTime()))
		{
			throw new ExpiredTokenException("The provided token can no longer be used");
		}
		updateState(t, TokenState.USED);
	}

	@Transactional
	public void revokeToken(final Long tokenId)
	{
		final Token t = repository.findByIdWithPessimisticLock(tokenId).get();
		updateState(t, TokenState.REVOKED);
	}

	private void updateState(final Long id, final TokenState state)
	{
		updateState(repository.findByIdWithPessimisticLock(id).get(), state);
	}

	private void updateState(final Token t, final TokenState state)
	{
		if (t.getState().isTransitionAllowed(state))
		{
			final TokenStateChange change = new TokenStateChange();
			change.setNewState(state);
			change.setPreviousState(t.getState());
			change.setCreated(systemDateTimeService.getCurrentDateTime());
			if (t.getStateChanges() == null)
			{
				t.setStateChanges(new ArrayList<>());
			}
			change.setToken(t);
			t.getStateChanges().add(change);
			t.setState(state);
			repository.save(t);
		}
		else
		{
			throw new IllegalArgumentException(String.format("Cannot change state from '%s' to '%s'", t.getState(), state));
		}
	}

	private String generateTokenCode(final int length)
	{
		return RandomStringUtils.randomAlphanumeric(length);
	}

	private String generateTokenCode(final int length, final String chars)
	{
		return RandomStringUtils.random(length, chars);
	}

	public int getOneTimeLoginTokenExpiryInSeconds()
	{
		return oneTimeLoginTokenExpiryInSeconds;
	}

	public int getPasswordResetTokenExpiryInSeconds()
	{
		return passwordResetTokenExpiryInSeconds;
	}
}
