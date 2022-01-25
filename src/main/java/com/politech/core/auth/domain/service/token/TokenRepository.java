package com.politech.core.auth.domain.service.token;

import com.politech.core.auth.domain.model.token.Token;
import com.politech.core.auth.domain.model.token.TokenState;
import com.politech.core.auth.domain.model.users.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends CrudRepository<Token, Long>, TokenRepositoryCustom
{

	Optional<Token> findByToken(final String token);

	Optional<Token> findByTokenIgnoreCase(final String token);

	List<Token> findByUserAndState(final User user, final TokenState state);
}
