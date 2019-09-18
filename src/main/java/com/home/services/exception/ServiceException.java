package com.home.services.exception;

import com.home.services.model.Error;

public class ServiceException extends Exception {
	private Error error;

	public ServiceException() {
		super();
	}

	public ServiceException(Error error) {
		super(error.getReason());
		this.error = error;

	}

	public ServiceException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public ServiceException(final String message) {
		super(message);
	}

	public ServiceException(final Throwable cause) {
		super(cause);
	}

	public Error getError() {
		return error;
	}

	public void setError(Error error) {
		this.error = error;
	}
}
