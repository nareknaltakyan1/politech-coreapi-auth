package com.politech.core.auth.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class OpenApiDocsConfig
{

	@ConditionalOnMissingBean
	@Bean
	public BuildProperties buildProperties() {
		Properties properties = new Properties();
		properties.put("group", "com.politech.api");
		properties.put("artifact", "politech-coreapi-auth");
		properties.put("version", "0.0.1-SNAPSHOT");
		return new BuildProperties(properties);
	}

	@Bean
	public OpenAPI customOpenAPI(final BuildProperties buildProperties)
	{
		return new OpenAPI()
			.components(new Components().addSecuritySchemes("bearer-key",
				new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")))
			.info(new Info().title("Authentication API").version(buildProperties.getVersion())
				.description("The Authentication API manages all information regarding users and organizations."
					+ " Its main functionality is to support authentication and authorization."));
	}
}
