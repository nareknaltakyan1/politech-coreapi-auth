package com.politech.core.auth.domain.model.users;

import lombok.*;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Embeddable
@Access(AccessType.FIELD)
@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class DynamicPassword
{

	@NotNull
	private Instant expiresAt;

	@NotNull
	private String password;

	public DynamicPassword(final Instant expiresAt, final String password)
	{
		this.expiresAt = expiresAt;
		this.password = password;
	}

	@Override
	public String toString()
	{
		return new ToStringBuilder(this).append("expiresAt", expiresAt).append("password", "******").toString();
	}
}
