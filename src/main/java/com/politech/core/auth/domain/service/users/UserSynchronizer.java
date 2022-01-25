package com.politech.core.auth.domain.service.users;

public interface UserSynchronizer
{
	void synchronize(boolean waitForCompletion);
}
