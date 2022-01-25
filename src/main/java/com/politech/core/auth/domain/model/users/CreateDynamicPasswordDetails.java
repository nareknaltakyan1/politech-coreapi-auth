package com.politech.core.auth.domain.model.users;

import lombok.Value;

import java.time.Instant;

@Value
public class CreateDynamicPasswordDetails
{

	Instant expiresAt;
	String password;
}
