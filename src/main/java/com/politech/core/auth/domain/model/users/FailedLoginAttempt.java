package com.politech.core.auth.domain.model.users;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "failedloginattempts")
public class FailedLoginAttempt
{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, updatable = false)
	private Long id;
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "userid", nullable = false)
	private User user;
	@Column(name = "ipaddress", updatable = false)
	private String ipAddress;
	@Column(name = "useragent", nullable = false, updatable = false)
	private String userAgent;
	@Column(name = "date", nullable = false, updatable = false)
	private LocalDateTime date;

	public Long getId()
	{
		return id;
	}

	public void setId(final Long id)
	{
		this.id = id;
	}

	public User getUser()
	{
		return user;
	}

	public void setUser(final User user)
	{
		this.user = user;
	}

	public String getIpAddress()
	{
		return ipAddress;
	}

	public void setIpAddress(final String ipAddress)
	{
		this.ipAddress = ipAddress;
	}

	public String getUserAgent()
	{
		return userAgent;
	}

	public void setUserAgent(final String userAgent)
	{
		this.userAgent = userAgent;
	}

	public LocalDateTime getDate()
	{
		return date;
	}

	public void setDate(LocalDateTime date)
	{
		this.date = date;
	}

	@Override
	public String toString()
	{
		return new ToStringBuilder(this).append("id", id).append("ipAddress", ipAddress).append("userAgent", userAgent).append("date", date)
			.toString();
	}
}
