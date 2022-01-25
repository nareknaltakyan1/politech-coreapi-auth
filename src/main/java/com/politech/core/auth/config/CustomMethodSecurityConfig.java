package com.politech.core.auth.config;

import com.politech.core.auth.infrastructure.common.security.CustomMethodSecurityExpressionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class CustomMethodSecurityConfig extends GlobalMethodSecurityConfiguration
{

	@Override
	protected MethodSecurityExpressionHandler createExpressionHandler()
	{
		return new CustomMethodSecurityExpressionHandler();
	}
}
