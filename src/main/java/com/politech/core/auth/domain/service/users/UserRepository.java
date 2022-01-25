package com.politech.core.auth.domain.service.users;

import com.politech.core.auth.domain.model.users.User;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface UserRepository extends PagingAndSortingRepository<User, Long>, QuerydslPredicateExecutor<User>
{

	Optional<User> findByEmail(final String email);

	boolean existsByEmail(String email);

	boolean existsByPhoneNumber(String phoneNumber);
}
