package com.politech.core.auth.config;

import com.politech.core.auth.infrastructure.common.validation.PasswordPatternValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeansConfig
{

	@Bean
	public PasswordPatternValidator passwordPatternValidator()
	{
		return new PasswordPatternValidator();
	}

}
