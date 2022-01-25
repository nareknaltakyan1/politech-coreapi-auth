package com.politech.core.auth.domain.model.token;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("ONE_TIME_LOGIN")
public class OneTimeLoginToken extends Token
{
	public OneTimeLoginToken()
	{
		super(TokenType.ONE_TIME_LOGIN);
	}
}
