package com.home.services.exception;

import org.springframework.http.HttpStatus;

import com.home.services.model.Error;

/**
 * Custom Exception for Validation errors.
 */
public class ValidationException extends ServiceException {
	public ValidationException() {
		super();
	}

	public ValidationException(Error error) {
		super(error);
	}

	public ValidationException(final String message, final Throwable cause) {
		super(message, cause);
		if (this.getError() == null) {
			this.setError(new Error(HttpStatus.BAD_REQUEST, message));
		}
	}

	public ValidationException(final String message) {
		super(message);
		if (this.getError() == null) {
			this.setError(new Error(HttpStatus.BAD_REQUEST, message));
		}
	}

	public ValidationException(final Throwable cause) {
		super(cause);
		if (this.getError() == null) {
			this.setError(new Error(HttpStatus.BAD_REQUEST, cause.getMessage()));
		}
	}
}
