package com.politech.core.auth.rest.exception;

//import ExpiredTokenException;
//import InvalidTokenException;
//import com.transferz.core.auth.domain.model.users.PasswordMismatchException;
//import com.transferz.core.auth.domain.model.users.UserNotExistsForEmailException;
//import com.transferz.core.common.presentation.rest.validation.*;
import com.politech.core.auth.domain.model.token.ExpiredTokenException;
import com.politech.core.auth.domain.model.token.InvalidTokenException;
import com.politech.core.auth.domain.model.users.PasswordMismatchException;
import com.politech.core.auth.domain.model.users.UserNotExistsForEmailException;
import com.politech.core.auth.infrastructure.common.validation.ErrorField;
import com.politech.core.auth.infrastructure.common.validation.ErrorMessage;
import com.politech.core.auth.infrastructure.common.validation.ErrorResponse;
import com.politech.core.auth.infrastructure.common.validation.UniqueConstraintException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler
{

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex, final HttpHeaders headers,
		final HttpStatus status, final WebRequest request)
	{
		final ErrorResponse errorResponse = new ErrorResponse();
		for (final FieldError error : ex.getBindingResult().getFieldErrors())
		{
			final ErrorMessage errorMessage = new ErrorMessage(HttpStatus.BAD_REQUEST, AuthErrorCode.fromValue(error.getDefaultMessage()),
				AuthErrorField.fromCode(error.getField()));
			errorResponse.addError(errorMessage);
		}
		for (final ObjectError error : ex.getBindingResult().getGlobalErrors())
		{
			final ErrorMessage errorMessage = new ErrorMessage(HttpStatus.BAD_REQUEST, AuthErrorCode.UNKNOWN,
				error.getObjectName() + ": " + error.getDefaultMessage());
			errorResponse.addError(errorMessage);
		}
		return handleExceptionInternal(ex, errorResponse, headers, HttpStatus.BAD_REQUEST, request);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> handleIllegalArgumentException(final IllegalArgumentException ex, final WebRequest request)
	{
		final ErrorResponse errorResponse = new ErrorResponse();
		final ErrorMessage errorMessage = new ErrorMessage(HttpStatus.BAD_REQUEST, AuthErrorCode.UNKNOWN, ex.getLocalizedMessage());
		errorResponse.addError(errorMessage);
		return new ResponseEntity<>(errorResponse, new HttpHeaders(), errorMessage.getHttpStatus());
	}

	@ExceptionHandler(InvalidPasswordFormatException.class)
	public ResponseEntity<ErrorResponse> handleIllegalArgumentException(final InvalidPasswordFormatException ex)
	{
		final ErrorResponse errorResponse = new ErrorResponse();
		final ErrorMessage errorMessage = new ErrorMessage(HttpStatus.BAD_REQUEST, AuthErrorCode.INVALID_PASSWORD_FORMAT,
			AuthErrorField.fromCode(ex.getField()));
		errorResponse.addError(errorMessage);
		return new ResponseEntity<>(errorResponse, new HttpHeaders(), errorMessage.getHttpStatus());
	}

	@ExceptionHandler(UniqueConstraintException.class)
	public ResponseEntity<ErrorResponse> handleIllegalArgumentException(final UniqueConstraintException ex)
	{
		final ErrorResponse errorResponse = new ErrorResponse();
		final ErrorMessage errorMessage = new ErrorMessage(HttpStatus.BAD_REQUEST, AuthErrorCode.UNIQUE_CONSTRAINT, AuthErrorField.fromCode(ex.getFieldName()));
		errorResponse.addError(errorMessage);
		return new ResponseEntity<>(errorResponse, new HttpHeaders(), errorMessage.getHttpStatus());
	}

	@ExceptionHandler(LockedException.class)
	public ResponseEntity<ErrorResponse> handleAccountIsLockedException(final LockedException ex)
	{
		final ErrorResponse errorResponse = new ErrorResponse();
		final ErrorMessage errorMessage = new ErrorMessage(HttpStatus.FORBIDDEN, AuthErrorCode.LOCKED_USER_ACCOUNT, ex.getLocalizedMessage());
		errorResponse.addError(errorMessage);
		return new ResponseEntity<>(errorResponse, errorMessage.getHttpStatus());
	}

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ErrorResponse> handleBadCredentialsException(final BadCredentialsException ex)
	{
		final ErrorResponse errorResponse = new ErrorResponse();
		final ErrorMessage errorMessage = new ErrorMessage(HttpStatus.UNAUTHORIZED, AuthErrorCode.BAD_CREDENTIALS, ex.getLocalizedMessage());
		errorResponse.addError(errorMessage);
		return new ResponseEntity<>(errorResponse, errorMessage.getHttpStatus());
	}

	@ExceptionHandler(ExpiredTokenException.class)
	public ResponseEntity<ErrorResponse> handleExpiredTokenException(final ExpiredTokenException ex)
	{
		final ErrorResponse errorResponse = new ErrorResponse();
		final ErrorMessage errorMessage = new ErrorMessage(HttpStatus.BAD_REQUEST, AuthErrorCode.EXPIRED_TOKEN, ex.getLocalizedMessage());
		errorResponse.addError(errorMessage);
		return new ResponseEntity<>(errorResponse, errorMessage.getHttpStatus());
	}

	@ExceptionHandler(UserNotExistsForEmailException.class)
	public ResponseEntity<ErrorResponse> handleWrongEmailException(final UserNotExistsForEmailException ex)
	{
		final ErrorResponse errorResponse = new ErrorResponse();
		final ErrorMessage errorMessage = new ErrorMessage(HttpStatus.BAD_REQUEST, AuthErrorCode.BAD_CREDENTIALS, (ErrorField) null);
		errorResponse.addError(errorMessage);
		return new ResponseEntity<>(errorResponse, errorMessage.getHttpStatus());
	}

	@ExceptionHandler(PasswordMismatchException.class)
	public ResponseEntity<ErrorResponse> handlePasswordMismatchException(final PasswordMismatchException ex)
	{
		final ErrorResponse errorResponse = new ErrorResponse();
		final ErrorMessage errorMessage = new ErrorMessage(HttpStatus.BAD_REQUEST, AuthErrorCode.PASSWORD_MISMATCH,
			AuthErrorField.fromCode(ex.getFieldName()));
		errorResponse.addError(errorMessage);
		return new ResponseEntity<>(errorResponse, errorMessage.getHttpStatus());
	}

	@ExceptionHandler(InvalidTokenException.class)
	public ResponseEntity<ErrorResponse> handleInvalidTokenException(final InvalidTokenException ex)
	{
		final ErrorResponse errorResponse = new ErrorResponse();
		final ErrorMessage errorMessage = new ErrorMessage(HttpStatus.BAD_REQUEST, AuthErrorCode.INVALID_TOKEN, AuthErrorField.PASSWORD_RESET_TOKEN);
		errorResponse.addError(errorMessage);
		return new ResponseEntity<>(errorResponse, errorMessage.getHttpStatus());
	}
}
