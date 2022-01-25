package com.politech.core.auth.domain.service.token;

import com.politech.core.auth.domain.model.token.Token;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import java.util.Optional;

public class TokenRepositoryCustomImpl implements TokenRepositoryCustom
{
	@PersistenceContext
	private final EntityManager entityManager;

	public TokenRepositoryCustomImpl(final EntityManager entityManager)
	{
		this.entityManager = entityManager;
	}

	@Override
	public Optional<Token> findByIdWithPessimisticLock(final Long id)
	{
		Assert.notNull(id, "Token id should not be null");
		entityManager.flush();
		final Token record = entityManager.find(Token.class, id, LockModeType.PESSIMISTIC_WRITE);
		if (record != null)
		{
			entityManager.refresh(record);
			return Optional.of(record);
		}
		return Optional.empty();
	}

}
