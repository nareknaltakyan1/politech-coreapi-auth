package com.politech.core.auth.infrastructure.common.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.politech.core.auth.infrastructure.common.security.SecurityConstants.HEADER_STRING;
import static com.politech.core.auth.infrastructure.common.security.SecurityConstants.TOKEN_PREFIX;

public class JwtAuthenticationFilter extends OncePerRequestFilter
{
	private static final Logger LOG = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Override
	protected void doFilterInternal(final HttpServletRequest req, final HttpServletResponse res, final FilterChain chain)
		throws IOException, ServletException
	{
		clearSecurityContext();
		final String authToken = getAuthToken(req);
		JwtUser user = null;
		if (authToken != null)
		{
			user = getUserFromToken(authToken, null);
		}
		else
		{
			LOG.trace("Couldn't find 'Bearer' string, ignoring authorization header");
		}
		if (user != null)
		{
			LOG.trace("User {}:{} authenticated", user.getEmail(), user.getId());
			final JwtAuthenticationToken authentication = new JwtAuthenticationToken(user, authToken, user.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(authentication);
			MDC.put("user", user.getEmail());
		}
		try
		{
			chain.doFilter(req, res);
		}
		finally
		{
			clearSecurityContext();
			MDC.clear();
		}
	}

	private void clearSecurityContext()
	{
		SecurityContextHolder.clearContext();
	}

	private JwtUser getUserFromToken(final String authToken, JwtUser user)
	{
		try
		{
			user = jwtTokenUtil.getUserFromToken(authToken);
		}
		catch (final IllegalArgumentException e)
		{
			LOG.error("An error occured during getting user from token", e);
		}
		catch (final ExpiredJwtException e)
		{
			LOG.warn(LOG.isDebugEnabled() ? String.format("Token '%s' is expired and not valid anymore", authToken)
				: "Token is expired and not valid anymore", e);
		}
		catch (final SignatureException e)
		{
			LOG.error("The signature is invalid");
		}
		return user;
	}

	private String getAuthToken(final HttpServletRequest req)
	{
		final String header = req.getHeader(HEADER_STRING);
		if (header != null && header.startsWith(TOKEN_PREFIX))
		{
			return header.replace(TOKEN_PREFIX, "");
		}
		return null;
	}
}
