package com.politech.core.auth.security;

import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Component
public class PasswordBasedKeyPasswordEncoder implements org.springframework.security.crypto.password.PasswordEncoder
{

	@Override
	public String encode(final CharSequence rawPassword)
	{
		try
		{
			return HashUtils.createHash(rawPassword.toString());
		}
		catch (InvalidKeySpecException | NoSuchAlgorithmException ex)
		{
			throw new IllegalStateException("Unable to hash password", ex);
		}
	}

	@Override
	public boolean matches(final CharSequence rawPassword, final String encodedPassword)
	{
		try
		{
			return HashUtils.validatePassword(rawPassword.toString().toCharArray(), encodedPassword);
		}
		catch (InvalidKeySpecException | NoSuchAlgorithmException ex)
		{
			throw new IllegalStateException("Unable to hash password", ex);
		}
	}

}
