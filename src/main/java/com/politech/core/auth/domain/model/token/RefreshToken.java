package com.politech.core.auth.domain.model.token;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("REFRESH")
public class RefreshToken extends Token
{
	public RefreshToken()
	{
		super(TokenType.REFRESH);
	}
}
