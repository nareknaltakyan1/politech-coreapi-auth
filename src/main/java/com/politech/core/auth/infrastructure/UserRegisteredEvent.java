package com.politech.core.auth.infrastructure;

public class UserRegisteredEvent
{

	private final Long userId;
	private String password;
	private boolean waitForDataSyncCompletion;
	private boolean sendEmail = true;

	private UserRegisteredEvent(final Long userId)
	{
		this.userId = userId;
	}

	public static UserRegisteredEvent of(final Long userId)
	{
		return new UserRegisteredEvent(userId);
	}

	public UserRegisteredEvent withRawPassword(String password)
	{
		setPassword(password);
		return this;
	}

	private void setPassword(final String password)
	{
		this.password = password;
	}

	public Long getUserId()
	{
		return userId;
	}

	public String getPassword()
	{
		return password;
	}

	public boolean isWaitForDataSyncCompletion()
	{
		return waitForDataSyncCompletion;
	}

	public UserRegisteredEvent withWaitForDataSyncCompletion(final boolean waitForDataSyncCompletion)
	{
		this.waitForDataSyncCompletion = waitForDataSyncCompletion;
		return this;
	}

	public UserRegisteredEvent withSendEmail(final boolean sendEmail)
	{
		this.sendEmail = sendEmail;
		return this;
	}

	public boolean isSendEmail()
	{
		return sendEmail;
	}
}
