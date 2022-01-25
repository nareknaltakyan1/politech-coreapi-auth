package com.politech.core.auth.domain.service.users;

import com.politech.core.auth.domain.model.users.UserPermission;
import org.springframework.data.repository.CrudRepository;

public interface UserRoleRepository extends CrudRepository<UserPermission, Long>
{
}
