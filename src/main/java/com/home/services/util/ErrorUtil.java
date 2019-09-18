package com.home.services.util;

import java.util.List;
import org.springframework.http.HttpStatus;

import com.home.services.model.Error;
import com.home.services.model.RootCause;

/**
 * Util class to handle Error related miscleneous functions.
 */
public class ErrorUtil {
	/**
	 * Return Error object
	 * @param status
	 * @param reason
	 * @param errors
	 * @return Error
	 */
	public static Error populateError(final HttpStatus status, final String reason, final List<RootCause> errors){
		return new Error(status, reason, errors);
	}
}
