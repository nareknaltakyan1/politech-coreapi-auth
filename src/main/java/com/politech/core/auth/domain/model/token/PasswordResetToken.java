package com.politech.core.auth.domain.model.token;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("PASSWORD_RESET")
public class PasswordResetToken extends Token
{
	public PasswordResetToken()
	{
		super(TokenType.PASSWORD_RESET);
	}
}
