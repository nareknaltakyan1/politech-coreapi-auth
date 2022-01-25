package com.politech.core.auth.domain.service.users;

import com.politech.core.auth.domain.model.users.FailedLoginAttempt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FailedLoginAttemptRepository extends JpaRepository<FailedLoginAttempt, Long>
{
}
