package com.politech.core.auth.domain.model.users;

import com.politech.core.auth.domain.model.organizations.Organization;

import java.time.LocalDateTime;

public class UserPermissionBuilder
{
	public GlobalUserRoleBuilder forGlobalRole(final Permission role)
	{
		return new GlobalUserRoleBuilder(role);
	}

	public OrganizationalUserRoleBuilder forOrganizationalRole(final Permission role)
	{
		return new OrganizationalUserRoleBuilder(role);
	}

	public class GlobalUserRoleBuilder extends AbstractUserRoleBuilder
	{
		GlobalUserRoleBuilder(final Permission role)
		{
			super(role);
		}

		public GlobalUserRoleBuilder withUser(final User user)
		{
			return (GlobalUserRoleBuilder) super.withUser(user);
		}

		public GlobalUserRoleBuilder withCreated(final LocalDateTime created)
		{
			return (GlobalUserRoleBuilder) super.withCreated(created);
		}

		public GlobalUserPermission build()
		{
			GlobalUserPermission r = new GlobalUserPermission();
			setValues(r);
			return r;
		}
	}

	public class OrganizationalUserRoleBuilder extends AbstractUserRoleBuilder
	{
		private Organization organization;

		OrganizationalUserRoleBuilder(final Permission role)
		{
			super(role);
		}

		public OrganizationalUserRoleBuilder withUser(final User user)
		{
			return (OrganizationalUserRoleBuilder) super.withUser(user);
		}

		public OrganizationalUserRoleBuilder withCreated(final LocalDateTime created)
		{
			return (OrganizationalUserRoleBuilder) super.withCreated(created);
		}

		public OrganizationalUserRoleBuilder withOrganization(final Organization organization)
		{
			this.organization = organization;
			return this;
		}

		public OrganizationalUserPermission build()
		{
			OrganizationalUserPermission r = new OrganizationalUserPermission();
			setValues(r);
			r.setOrganization(organization);
			return r;
		}
	}

	class AbstractUserRoleBuilder
	{
		private Permission permission;
		private User user;
		private LocalDateTime created;

		AbstractUserRoleBuilder(final Permission permission)
		{
			this.permission = permission;
		}

		void setValues(final UserPermission userPermission)
		{
			userPermission.setCreated(created);
			userPermission.setPermission(permission);
			userPermission.setUser(user);
		}

		AbstractUserRoleBuilder withUser(final User user)
		{
			this.user = user;
			return this;
		}

		AbstractUserRoleBuilder withCreated(final LocalDateTime created)
		{
			this.created = created;
			return this;
		}
	}
}
