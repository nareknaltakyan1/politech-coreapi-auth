package com.politech.core.auth.domain.model.users;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "users", indexes = { @Index(name = "users_updated_index", columnList = "updated") })
public class User
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String email;
	private boolean emailVerified;
	private String phoneNumber;
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private final List<FailedLoginAttempt> failedLoginAttempts = new ArrayList<>();
	@Enumerated(EnumType.STRING)
	private UserAccountState accountState;
	private String saltedPasswordHash;
	@Column(nullable = false)
	private LocalDateTime created;
	@Column(nullable = false)
	private LocalDateTime updated;
	private LocalDateTime lastLogin;
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private List<UserPermission> permissions;
	@Convert(converter = LocaleConverter.class)
	private Locale locale;

	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "expiresAt", column = @Column(name = "dynamicpasswordexpiresat")),
		@AttributeOverride(name = "password", column = @Column(name = "dynamicpassword")) })
	private DynamicPassword dynamicPassword;

	public Long getId()
	{
		return id;
	}

	public void setId(final Long id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(final String name)
	{
		this.name = name;
	}

	public List<FailedLoginAttempt> getFailedLoginAttempts()
	{
		if (failedLoginAttempts.isEmpty())
		{
			return Collections.emptyList();
		}
		return Collections.unmodifiableList(failedLoginAttempts);
	}

	public void addFailedLoginAttempt(final FailedLoginAttempt failedLoginAttempt)
	{
		Assert.notNull(failedLoginAttempt, "failedLoginAttempt should not be null");
		failedLoginAttempt.setUser(this);
		failedLoginAttempts.add(failedLoginAttempt);
	}

	public void clearAllFailedLoginAttempts()
	{
		failedLoginAttempts.removeIf(Objects::nonNull);
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(final String email)
	{
		this.email = email;
	}

	public String getPhoneNumber()
	{
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber)
	{
		this.phoneNumber = phoneNumber;
	}

	public LocalDateTime getCreated()
	{
		return created;
	}

	public LocalDateTime getUpdated()
	{
		return updated;
	}

	public LocalDateTime getLastLogin()
	{
		return lastLogin;
	}

	public void setLastLogin(final LocalDateTime lastLogin)
	{
		this.lastLogin = lastLogin;
	}

	public String getSaltedPasswordHash()
	{
		return saltedPasswordHash;
	}

	public void setSaltedPasswordHash(final String saltedPasswordHash)
	{
		this.saltedPasswordHash = saltedPasswordHash;
	}

	public List<UserPermission> getPermissions()
	{
		return permissions;
	}

	public List<GlobalUserPermission> getGlobalPermissions()
	{
		if (permissions == null)
		{
			return Collections.emptyList();
		}
		return permissions.stream().filter(r -> r.getType() == PermissionType.GLOBAL).map(r -> (GlobalUserPermission) r)
			.collect(Collectors.toUnmodifiableList());
	}

	public List<OrganizationalUserPermission> getOrganizationalPermissions()
	{
		if (permissions == null)
		{
			return Collections.emptyList();
		}
		return permissions.stream().filter(r -> r.getType() == PermissionType.ORGANIZATIONAL).map(r -> (OrganizationalUserPermission) r)
			.collect(Collectors.toUnmodifiableList());
	}

	public void setPermissions(final List<UserPermission> permissions)
	{
		this.permissions = permissions;
	}

	public boolean isEmailVerified()
	{
		return emailVerified;
	}

	public void setEmailVerified(final boolean emailVerified)
	{
		this.emailVerified = emailVerified;
	}

	public UserAccountState getAccountState()
	{
		return accountState;
	}

	public void setAccountState(final UserAccountState accountState)
	{
		this.accountState = accountState;
	}

	public Locale getLocale()
	{
		return locale;
	}

	public void setLocale(Locale locale)
	{
		this.locale = locale;
	}

	public void setCreated(LocalDateTime created)
	{
		this.created = created;
	}

	public void setUpdated(LocalDateTime updated)
	{
		this.updated = updated;
	}

	public DynamicPassword getDynamicPassword()
	{
		return dynamicPassword;
	}

	public void setDynamicPassword(final DynamicPassword dynamicPassword)
	{
		this.dynamicPassword = dynamicPassword;
	}

	public void lockAccount()
	{
		this.accountState = UserAccountState.LOCKED;
	}

	@Override
	public String toString()
	{
		return new ToStringBuilder(this).append("id", id).append("name", name).append("email", email).append("emailVerified", emailVerified)
			.append("failedLoginAttempts", failedLoginAttempts).append("accountState", accountState)
			.append("saltedPasswordHash", "************************").append("created", created).append("updated", updated)
			.append("lastLogin", lastLogin).append("permissions", permissions).append("locale", locale).append("dynamicPassword", dynamicPassword)
			.append("phoneNumber", phoneNumber).build();
	}
}
