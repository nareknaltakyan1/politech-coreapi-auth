package com.politech.core.auth.domain.model.users;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Locale;

public class GetAllUsersDetails
{

	private String name;
	private String email;
	private boolean emailExactMatch;
	private Integer invalidLoginAttempts;
	private UserAccountState accountState;
	private Boolean emailVerified;
	private String userCompanyName;
	private String locale;
	private LocalDate createdFrom;
	private LocalDate createdTo;
	private Long organizationId;
	private LocalDateTime updatedAfter;

	public String getName()
	{
		return name;
	}

	public void setName(final String name)
	{
		this.name = name;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(final String email)
	{
		this.email = email;
	}

	public Integer getInvalidLoginAttempts()
	{
		return invalidLoginAttempts;
	}

	public void setInvalidLoginAttempts(final Integer invalidLoginAttempts)
	{
		this.invalidLoginAttempts = invalidLoginAttempts;
	}

	public UserAccountState getAccountState()
	{
		return accountState;
	}

	public void setAccountState(final UserAccountState accountState)
	{
		this.accountState = accountState;
	}

	public Boolean getEmailVerified()
	{
		return emailVerified;
	}

	public void setEmailVerified(final Boolean emailVerified)
	{
		this.emailVerified = emailVerified;
	}

	public String getUserCompanyName()
	{
		return userCompanyName;
	}

	public void setUserCompanyName(final String userCompanyName)
	{
		this.userCompanyName = userCompanyName;
	}

	public Locale getLocale()
	{
		return locale == null ? null : Locale.forLanguageTag(locale);
	}

	public void setLocale(final String locale)
	{
		this.locale = locale;
	}

	public LocalDateTime getCreatedFrom()
	{
		return createdFrom == null ? null : createdFrom.atStartOfDay();
	}

	public void setCreatedFrom(final LocalDate createdFrom)
	{
		this.createdFrom = createdFrom;
	}

	public LocalDateTime getCreatedTo()
	{
		return createdTo == null ? null : LocalDateTime.of(createdTo, LocalTime.now());
	}

	public void setCreatedTo(final LocalDate createdTo)
	{
		this.createdTo = createdTo;
	}

	public Long getOrganizationId()
	{
		return organizationId;
	}

	public void setOrganizationId(final Long organizationId)
	{
		this.organizationId = organizationId;
	}

	public LocalDateTime getUpdatedAfter()
	{
		return updatedAfter;
	}

	public void setUpdatedAfter(final LocalDateTime updatedAfter)
	{
		this.updatedAfter = updatedAfter;
	}

	public boolean isEmailExactMatch()
	{
		return emailExactMatch;
	}

	public void setEmailExactMatch(final boolean emailExactMatch)
	{
		this.emailExactMatch = emailExactMatch;
	}
}
