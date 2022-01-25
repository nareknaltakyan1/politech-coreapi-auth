package com.politech.core.auth.domain.service.system;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SystemDateTimeService
{

	public LocalDateTime getCurrentDateTime()
	{
		return LocalDateTime.now();
	}
}
