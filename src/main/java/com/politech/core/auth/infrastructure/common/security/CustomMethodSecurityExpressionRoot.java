package com.politech.core.auth.infrastructure.common.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import static com.politech.core.auth.infrastructure.common.security.SecurityConstants.*;

public class CustomMethodSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations
{
	private static final Logger LOG = LoggerFactory.getLogger(CustomMethodSecurityExpressionRoot.class);

	private Object filterObject;
	private Object returnObject;

	public CustomMethodSecurityExpressionRoot(final Authentication authentication)
	{
		super(authentication);
	}

	@Deprecated
	public boolean isInternalSystem()
	{
		final Authentication authentication = getAuthentication();
		if (authentication != null && authentication.getPrincipal() instanceof JwtUser
//				todo change email
			&& (((JwtUser) authentication.getPrincipal()).getEmail().endsWith("email") || hasGlobalPermission("INTERNAL_SYSTEM")))
		{
			return true;
		}
		return false;
	}

	@Deprecated
	public boolean hasGlobalRole(final String role)
	{
		return hasGlobalPermission(role);
	}

	public boolean hasGlobalPermission(final String permission)
	{
		final Authentication authentication = getAuthentication();
		if (authentication == null)
		{
			return false;
		}
		final String fullPermission = String.format(GLOBAL_PERMISSION_FORMAT, permission);
		for (final GrantedAuthority grantedAuth : authentication.getAuthorities())
		{
			if (grantedAuth.getAuthority().equals(fullPermission))
			{
				return true;
			}
		}
		return false;
	}

	@Deprecated
	public boolean hasOrgRole(final String role)
	{
		return hasOrgPermission(role);
	}

	public boolean hasOrgPermission(final String permission)
	{
		final Authentication authentication = getAuthentication();
		if (authentication == null)
		{
			return false;
		}
		final String prefix = String.format("%s%s", ORGANIZATIONAL_PERMISSION_PREFIX, permission);
		for (final GrantedAuthority grantedAuth : authentication.getAuthorities())
		{
			if (grantedAuth.getAuthority().startsWith(prefix))
			{
				return true;
			}
		}
		return false;
	}

	@Deprecated
	public boolean hasOrgRole(final String role, final Long organizationId)
	{
		return hasOrgPermission(role, organizationId);
	}

	public boolean hasOrgPermission(final String permission, final Long organizationId)
	{
		final Authentication authentication = getAuthentication();
		if (authentication == null || organizationId == null)
		{
			return false;
		}
		final String fullPermission = String.format(ORGANIZATIONAL_PERMISSION_FORMAT, permission, organizationId);
		for (final GrantedAuthority grantedAuth : authentication.getAuthorities())
		{
			if (grantedAuth.getAuthority().equals(fullPermission))
			{
				return true;
			}
		}
		return false;
	}

	public boolean isUser(final Long userId)
	{
		final Authentication authentication = getAuthentication();
		if (authentication == null || userId == null)
		{
			return false;
		}
		JwtUser user = (JwtUser) authentication.getPrincipal();
		return userId.equals(user.getId());
	}

	public boolean isOrgMember(final Long organizationId)
	{
		final Authentication authentication = getAuthentication();
		if (authentication == null || organizationId == null)
		{
			return false;
		}
		return hasOrganizationalPermission(organizationId, authentication);
	}

	private boolean hasOrganizationalPermission(final Long organizationId, final Authentication authentication)
	{
		for (GrantedAuthority grantedAuthority : authentication.getAuthorities())
		{
			final String permission = grantedAuthority.getAuthority();
			if (permission.startsWith(ORGANIZATIONAL_PERMISSION_PREFIX)
				&& organizationId.equals(getOrganizationIdFromPermission(permission)))
			{
				return true;
			}
		}
		return false;
	}

	private Long getOrganizationIdFromPermission(final String role)
	{
		try
		{
			return Long.valueOf(role.substring(role.indexOf(':') + 1));
		}
		catch (NumberFormatException | IndexOutOfBoundsException ex)
		{
			LOG.error("Unable to obtain organization id from role '{}'", role);
			return null;
		}
	}

	@Override
	public void setFilterObject(final Object filterObject)
	{
		this.filterObject = filterObject;
	}

	@Override
	public Object getFilterObject()
	{
		return filterObject;
	}

	@Override
	public void setReturnObject(final Object returnObject)
	{
		this.returnObject = returnObject;
	}

	@Override
	public Object getReturnObject()
	{
		return returnObject;
	}

	@Override
	public Object getThis()
	{
		return this;
	}

}
