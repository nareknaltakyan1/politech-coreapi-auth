package com.politech.core.auth.security;

import com.politech.core.auth.domain.model.users.User;
import com.politech.core.auth.domain.service.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ApplicationUserDetailsService implements UserDetailsService
{
	@Autowired
	private UserService userService;

	@Override
	public ApplicationUserDetails loadUserByUsername(final String username) throws UsernameNotFoundException
	{
		final User user = userService.findByEmail(username);
		return new ApplicationUserDetails(user);
	}
}
