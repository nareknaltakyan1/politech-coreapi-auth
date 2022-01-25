package com.politech.core.auth.domain.service.token;

import com.politech.core.auth.domain.model.token.Token;

import java.util.Optional;

public interface TokenRepositoryCustom
{
	Optional<Token> findByIdWithPessimisticLock(final Long id);
}
