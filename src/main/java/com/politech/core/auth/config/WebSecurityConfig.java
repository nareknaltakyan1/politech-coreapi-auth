package com.politech.core.auth.config;

//import com.transferz.core.auth.security.ApplicationUserDetailsService;
//import com.transferz.core.auth.security.PasswordBasedKeyPasswordEncoder;
//import com.transferz.core.common.application.security.JwtAuthenticationEntryPoint;
//import com.transferz.core.common.application.security.JwtAuthenticationFilter;
//import com.transferz.core.common.application.security.JwtTokenUtil;
import com.politech.core.auth.infrastructure.common.security.JwtAuthenticationEntryPoint;
import com.politech.core.auth.infrastructure.common.security.JwtAuthenticationFilter;
import com.politech.core.auth.infrastructure.common.security.JwtTokenUtil;
import com.politech.core.auth.security.ApplicationUserDetailsService;
import com.politech.core.auth.security.PasswordBasedKeyPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter
{

	@Resource
	private ApplicationUserDetailsService userDetailsService;

	@Autowired
	private JwtAuthenticationEntryPoint unauthorizedHandler;

	@Autowired
	private PasswordBasedKeyPasswordEncoder passwordEncoder;

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception
	{
		return super.authenticationManagerBean();
	}

	@Bean
	public JwtAuthenticationEntryPoint jwtAuthenticationEntryPointBean() throws Exception
	{
		return new JwtAuthenticationEntryPoint();
	}

	@Autowired
	public void globalUserDetails(final AuthenticationManagerBuilder auth)
	{
		final DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
		daoAuthenticationProvider.setUserDetailsService(userDetailsService);
		daoAuthenticationProvider.setPreAuthenticationChecks(userDetails ->
		{
		});
		daoAuthenticationProvider.setPostAuthenticationChecks(WebSecurityConfig::postAuthenticationChecks);
		auth.authenticationProvider(daoAuthenticationProvider);
	}

	@Bean
	public JwtAuthenticationFilter authenticationTokenFilterBean()
	{
		return new JwtAuthenticationFilter();
	}

	@Bean
	public JwtTokenUtil jwtTokenUtil()
	{
		return new JwtTokenUtil();
	}

	@Override
	protected void configure(final HttpSecurity http) throws Exception
	{
		http.cors().and().csrf().disable().authorizeRequests()
			.antMatchers("/auth/*", "/auth/auth-code/exchange", "/actuator/*", "/password-reset", "/password-reset/*",
					"/swagger*", "/v3/api-docs", "/swagger-ui.html",
				"/v3/api-docs/*", "/swagger-ui/*")
			.permitAll().anyRequest().authenticated().and().exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
	}

	private static void postAuthenticationChecks(UserDetails user)
	{
		if (!user.isAccountNonLocked())
		{
			throw new LockedException("User account is locked");
		}

		if (!user.isEnabled())
		{
			throw new DisabledException("User is disabled");
		}

		if (!user.isAccountNonExpired())
		{
			throw new AccountExpiredException("User account has expired");
		}
	}
}
