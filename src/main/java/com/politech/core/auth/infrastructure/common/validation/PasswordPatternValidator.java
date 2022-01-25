package com.politech.core.auth.infrastructure.common.validation;

import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class PasswordPatternValidator implements ConstraintValidator<Password, String>
{

	private static final String EXPRESSION = "^(?=[^A-Za-z]*[A-Za-z])(?=[^0-9]*[0-9])(?:([\\w\\d*!@#$~&-.%+,])\\1?(?!\\1))+$";

	private static final int MIN_LENGTH = 8;

	private static final int MAX_LENGTH = 64;

	private static final Pattern PATTERN = Pattern.compile(EXPRESSION);

	@Override
	public boolean isValid(final String value, final ConstraintValidatorContext constraintValidatorContext)
	{
		if (StringUtils.hasText(value) || notValidLength(value))
		{
			return false;
		}
		return PATTERN.matcher(value).matches();
	}

	private static boolean notValidLength(final String value)
	{
		return value.length() < MIN_LENGTH || value.length() > MAX_LENGTH;
	}
}
