package com.politech.core.auth.domain.model.users;

import org.springframework.util.Assert;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
@Table(name = "userpermission", uniqueConstraints = @UniqueConstraint(columnNames = { "userId", "permission",
	"organizationId" }, name = "user_organization_permission_unique"))
public abstract class UserPermission
{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long id;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, updatable = false, insertable = false)
	private PermissionType type;
	@Enumerated(EnumType.STRING)
	protected Permission permission;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId")
	protected User user;
	@Column(nullable = false)
	protected LocalDateTime created;

	public UserPermission()
	{
	}

	public UserPermission(PermissionType roleType)
	{
		this.type = roleType;
	}

	public Long getId()
	{
		return id;
	}

	public void setId(final Long id)
	{
		Assert.notNull(id, "Id must be specified");
		this.id = id;
	}

	public Permission getPermission()
	{
		return permission;
	}

	public void setPermission(final Permission permission)
	{
		Assert.notNull(permission, "Permission must be specified");
		Assert.isTrue(permission.getType() == type, String.format("Permission must be of type %s", type));
		this.permission = permission;
	}

	public User getUser()
	{
		return user;
	}

	public void setUser(final User user)
	{
		Assert.notNull(user, "User must be specified");
		this.user = user;
	}

	public LocalDateTime getCreated()
	{
		return created;
	}

	public void setCreated(LocalDateTime created)
	{
		this.created = created;
	}

	public PermissionType getType()
	{
		return type;
	}
}
