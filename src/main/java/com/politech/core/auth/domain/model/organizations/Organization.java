package com.politech.core.auth.domain.model.organizations;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "organization", indexes = { @Index(name = "organization_updated_index", columnList = "updated") })
public class Organization
{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	@Column(nullable = false)
	private LocalDateTime created;
	@Column(nullable = false)
	private LocalDateTime updated;

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

	public LocalDateTime getCreated()
	{
		return created;
	}

	public LocalDateTime getUpdated()
	{
		return updated;
	}

	public void setCreated(LocalDateTime created)
	{
		this.created = created;
	}

	public void setUpdated(LocalDateTime updated)
	{
		this.updated = updated;
	}
}
