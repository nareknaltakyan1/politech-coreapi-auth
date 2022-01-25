package com.politech.core.auth.infrastructure.common.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.politech.core.auth.infrastructure.common.security.SecurityConstants.*;

public class JwtTokenUtil
{
	public JwtUser getUserFromToken(final String token)
	{
		final Claims claims = getClaims(token);
		final String email = (String) claims.get(Claims.SUBJECT);
		final Long id = Long.valueOf(claims.get("user_id").toString());
		final String name = (String) claims.get("user_name");
		return new JwtUser(id, email, name, getAuthoritiesFromClaims(claims));
	}

	public Date getExpirationDateFromToken(final String token)
	{
		return getClaimFromToken(token, Claims::getExpiration);
	}

	public <T> T getClaimFromToken(final String token, final Function<Claims, T> claimsResolver)
	{
		final Claims claims = getClaims(token);
		return claimsResolver.apply(claims);
	}

	public boolean isTokenExpired(final String token)
	{
		try
		{
			getClaimFromToken(token, Claims::getExpiration);
		}
		catch (ExpiredJwtException e)
		{
			return true;
		}
		return false;
	}

	private Collection<? extends GrantedAuthority> getAuthoritiesFromClaims(final Claims claims)
	{
		return Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(",")).filter(StringUtils::isNotBlank).map(SimpleGrantedAuthority::new)
			.collect(Collectors.toList());
	}

	private Claims getClaims(final String token)
	{
		return Jwts.parser().setSigningKey(SIGNING_KEY).parseClaimsJws(token).getBody();
	}

	public String generateToken(final JwtUser user)
	{
		final String authoritiesString = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
		return Jwts.builder().setSubject(user.getEmail()).claim(AUTHORITIES_KEY, authoritiesString).claim("user_id", user.getId())
			.claim("user_name", user.getName()).signWith(SignatureAlgorithm.HS256, SIGNING_KEY).setIssuedAt(new Date(System.currentTimeMillis()))
			.setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY_SECONDS * 1000)).compact();
	}
}
