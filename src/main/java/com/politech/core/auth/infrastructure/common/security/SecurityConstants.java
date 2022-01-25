package com.politech.core.auth.infrastructure.common.security;

public class SecurityConstants
{

	public static final int ACCESS_TOKEN_VALIDITY_SECONDS = 5 * 60;
	public static final String SIGNING_KEY = "taxi2airport!78#";
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";
	public static final String AUTHORITIES_KEY = "scopes";
	public static final String GLOBAL_PERMISSION_PREFIX = "GLOBAL_";
	public static final String GLOBAL_PERMISSION_FORMAT = GLOBAL_PERMISSION_PREFIX + "%s";
	public static final String ORGANIZATIONAL_PERMISSION_PREFIX = "ORG_";
	public static final String ORGANIZATIONAL_PERMISSION_FORMAT = ORGANIZATIONAL_PERMISSION_PREFIX + "%s:%d";
	@Deprecated
	public static final String GLOBAL_ROLE_PREFIX = GLOBAL_PERMISSION_PREFIX;
	@Deprecated
	public static final String GLOBAL_ROLE_FORMAT = GLOBAL_PERMISSION_FORMAT;
	@Deprecated
	public static final String ORGANIZATIONAL_ROLE_PREFIX = ORGANIZATIONAL_PERMISSION_PREFIX;
	@Deprecated
	public static final String ORGANIZATIONAL_ROLE_FORMAT = ORGANIZATIONAL_PERMISSION_FORMAT;
	public static final long PASSWORD_RESET_EXIRY_SECONDS = 30 * 60;
}
