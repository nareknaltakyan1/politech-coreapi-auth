package com.politech.core.auth.infrastructure.common.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = PasswordPatternValidator.class)
@Documented
public @interface Password
{
	String message() default "INVALID_PASSWORD_FORMAT";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
