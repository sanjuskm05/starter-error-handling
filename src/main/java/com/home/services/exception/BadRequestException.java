package com.home.services.exception;

import org.springframework.http.HttpStatus;

import com.home.services.model.Error;

/**
 * Custom Excpetion to throw bad request errors.
 */
public class BadRequestException extends ServiceException {
	public BadRequestException() {
		super();
	}

	public BadRequestException(Error error) {
		super(error);
	}

	public BadRequestException(final String message, final Throwable cause) {
		super(message, cause);
		if (this.getError() == null) {
			this.setError(new Error(HttpStatus.BAD_REQUEST, message));
		}
	}

	public BadRequestException(final String message) {
		super(message);
		if (this.getError() == null) {
			this.setError(new Error(HttpStatus.BAD_REQUEST, message));
		}
	}

	public BadRequestException(final Throwable cause) {
		super(cause);
		if (this.getError() == null) {
			this.setError(new Error(HttpStatus.BAD_REQUEST, cause.getMessage()));
		}
	}

}