package com.politech.core.auth.security;

//import com.transferz.core.auth.domain.model.users.User;
//import com.transferz.core.auth.domain.model.users.UserAccountState;
import com.politech.core.auth.domain.model.users.User;
import com.politech.core.auth.domain.model.users.UserAccountState;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class ApplicationUserDetails implements UserDetails
{

	private static final long serialVersionUID = -5643001288776416983L;

	private final String email;
	private final String saltedPasswordHash;
	private final Collection<? extends GrantedAuthority> authorities;
	private final UserAccountState state;

	public ApplicationUserDetails(final User user)
	{
		this.saltedPasswordHash = user.getSaltedPasswordHash();
		this.authorities = PermissionToAuthorityConverter.convertPermissionsToGrantedAuthorities(user.getPermissions());
		this.email = user.getEmail();
		this.state = user.getAccountState();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities()
	{
		return authorities;
	}

	@Override
	public String getPassword()
	{
		return saltedPasswordHash;
	}

	@Override
	public String getUsername()
	{
		return email;
	}

	@Override
	public boolean isAccountNonExpired()
	{
		// Account expiry is not supported yet
		return true;
	}

	@Override
	public boolean isAccountNonLocked()
	{
		return state != UserAccountState.LOCKED;
	}

	@Override
	public boolean isCredentialsNonExpired()
	{
		// Credentials expiry is not supported yet
		return true;
	}

	@Override
	public boolean isEnabled()
	{
		return state == UserAccountState.ACTIVE;
	}

}
