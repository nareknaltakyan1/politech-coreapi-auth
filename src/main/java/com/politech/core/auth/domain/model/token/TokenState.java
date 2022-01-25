package com.politech.core.auth.domain.model.token;

public enum TokenState
{
	ISSUED, USED, REVOKED, EXPIRED;

	public boolean isTransitionAllowed(final TokenState newState)
	{
		switch (newState)
		{
			case ISSUED:
				// ISSUED is only allowed as an initial state
				return false;
			case USED:
			case EXPIRED:
			case REVOKED:
				// These states can only be transitioned to if the current state is ISSUED
				return this == ISSUED;
			default:
				return false;
		}
	}
}
