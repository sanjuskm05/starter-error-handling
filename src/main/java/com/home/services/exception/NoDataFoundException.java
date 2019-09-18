package com.home.services.exception;

import org.springframework.http.HttpStatus;

import com.home.services.model.Error;

/**
 * Custom Exception for NO data found.
 */
public class NoDataFoundException extends ServiceException {

	public NoDataFoundException() {
		super();
	}

	public NoDataFoundException(Error error) {
		super(error);
	}

	public NoDataFoundException(final String message, final Throwable cause) {
		super(message, cause);
		if (this.getError() == null) {
			this.setError(new Error(HttpStatus.NOT_FOUND, message));
		}
	}

	public NoDataFoundException(final String message) {
		super(message);
		if (this.getError() == null) {
			this.setError(new Error(HttpStatus.NOT_FOUND, message));
		}
	}

	public NoDataFoundException(final Throwable cause) {
		super(cause);
		if (this.getError() == null) {
			this.setError(new Error(HttpStatus.NOT_FOUND, cause.getMessage()));
		}
	}

}
