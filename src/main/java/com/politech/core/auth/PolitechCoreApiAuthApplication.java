package com.politech.core.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.annotation.PostConstruct;

@Slf4j
@EnableAsync
@SpringBootApplication
public class PolitechCoreApiAuthApplication
{

	public static void main(String[] args)
	{
		SpringApplication.run(PolitechCoreApiAuthApplication.class, args);
	}

	@PostConstruct
	public void afterStart()
	{
		log.info("Application started");
	}

}
