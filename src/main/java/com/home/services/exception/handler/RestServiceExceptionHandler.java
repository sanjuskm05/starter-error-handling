package com.home.services.exception.handler;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.home.services.exception.NoDataFoundException;
import com.home.services.exception.ServiceException;
import com.home.services.exception.ValidationException;
import com.home.services.model.Error;
import com.home.services.model.ErrorData;
import com.home.services.model.RootCause;
import com.home.services.util.ErrorUtil;

@ControllerAdvice
@EnableWebMvc
public class RestServiceExceptionHandler extends ResponseEntityExceptionHandler {
	private Logger logger = LoggerFactory.getLogger(RestServiceExceptionHandler.class);

	/**
	 * Overriding the existing handleExceptionInternal
	 * which is invoked in most of the exception handlers,
	 * but as we need to populate the response object in
	 * error scenario with custom response, this method is
	 * required.
	 *
	 * @param ex
	 * @param body
	 * @param headers
	 * @param status
	 * @param request
	 * @return ResponseEntity
	 */
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
			request.setAttribute("javax.servlet.error.exception", ex, 0);
		}
		ErrorData errorData = new ErrorData(ErrorUtil.populateError(status, status.getReasonPhrase(),
				Arrays.asList(new RootCause(status.value(), ex.getLocalizedMessage()))));
		populateErrorData(ex, errorData);
		return new ResponseEntity<Object>(errorData, headers, errorData.getError().getStatus());
	}

	/**
	 * Populate custom error data in exception scenario.
	 *
	 * @param ex
	 * @param errorData
	 */
	private void populateErrorData(Exception ex, ErrorData errorData) {
		StringBuilder builder = new StringBuilder();
		List<RootCause> errors = new ArrayList<RootCause>();
		if (ex instanceof MethodArgumentNotValidException) {
			MethodArgumentNotValidException methodArgumentNotValidException = (MethodArgumentNotValidException) ex;
			for (final FieldError error : methodArgumentNotValidException.getBindingResult().getFieldErrors()) {
				RootCause rootCause = new RootCause(Integer.valueOf(error.getCode()).intValue(),
						error.getDefaultMessage());
				errors.add(rootCause);
			}
			for (final ObjectError error : methodArgumentNotValidException.getBindingResult().getGlobalErrors()) {
				RootCause rootCause = new RootCause(Integer.valueOf(error.getCode()).intValue(),
						error.getDefaultMessage());
				errors.add(rootCause);
			}
			errorData.setError(new Error(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors));
		} else if (ex instanceof BindException) {
			BindException bindException = (BindException) ex;
			for (final FieldError error : bindException.getBindingResult().getFieldErrors()) {
				RootCause rootCause = new RootCause(Integer.valueOf(error.getCode()).intValue(),
						error.getDefaultMessage());
				errors.add(rootCause);
			}
			for (final ObjectError error : bindException.getBindingResult().getGlobalErrors()) {
				RootCause rootCause = new RootCause(Integer.valueOf(error.getCode()).intValue(),
						error.getDefaultMessage());
				errors.add(rootCause);
			}
			errorData.setError(new Error(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors));
		} else if (ex instanceof TypeMismatchException) {
			TypeMismatchException typeMismatchException = (TypeMismatchException) ex;
			errors.add(new RootCause(HttpStatus.BAD_REQUEST.value(),
					typeMismatchException.getValue() + " value for " + typeMismatchException.getPropertyName()
							+ " should be of type " + typeMismatchException.getRequiredType()));
			errorData.setError(new Error(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors));
		} else if (ex instanceof MissingServletRequestPartException) {
			MissingServletRequestPartException missingServletRequestPartException = (MissingServletRequestPartException) ex;
			errorData.setError(new Error(HttpStatus.BAD_REQUEST,
					missingServletRequestPartException.getRequestPartName() + " part is missing"));
		} else if (ex instanceof MissingServletRequestParameterException) {
			MissingServletRequestParameterException missingServletRequestParameterException = (MissingServletRequestParameterException) ex;
			errorData.setError(new Error(HttpStatus.BAD_REQUEST,
					missingServletRequestParameterException.getParameterName() + " parameter is missing"));
		} else if (ex instanceof MethodArgumentTypeMismatchException) {
			MethodArgumentTypeMismatchException methodArgumentTypeMismatchException = (MethodArgumentTypeMismatchException) ex;
			errorData.setError(new Error(HttpStatus.BAD_REQUEST,
					methodArgumentTypeMismatchException.getName() + " should be of type "
							+ methodArgumentTypeMismatchException.getRequiredType().getName()));
		} else if (ex instanceof HttpMediaTypeNotSupportedException) {
			HttpMediaTypeNotSupportedException httpMediaTypeNotSupportedException = (HttpMediaTypeNotSupportedException) ex;
			builder.append(httpMediaTypeNotSupportedException.getContentType());
			builder.append(" media type is not supported. Supported media types are ");
			httpMediaTypeNotSupportedException.getSupportedMediaTypes().forEach(t -> builder.append(t + " "));
			errorData
					.setError(new Error(HttpStatus.UNSUPPORTED_MEDIA_TYPE, builder.substring(0, builder.length() - 2)));
		} else if (ex instanceof HttpRequestMethodNotSupportedException) {
			HttpRequestMethodNotSupportedException httpRequestMethodNotSupportedException = (HttpRequestMethodNotSupportedException) ex;
			builder.append(httpRequestMethodNotSupportedException.getMethod());
			builder.append(" method is not supported for this request. Supported methods are ");
			httpRequestMethodNotSupportedException.getSupportedHttpMethods().forEach(t -> builder.append(t + " "));
			builder.append(httpRequestMethodNotSupportedException.getMethod());
			errorData.setError(new Error(HttpStatus.METHOD_NOT_ALLOWED, builder.toString()));
		} else if (ex instanceof ConstraintViolationException) {
			ConstraintViolationException constraintViolationException = (ConstraintViolationException) ex;
			for (final ConstraintViolation<?> violation : constraintViolationException.getConstraintViolations()) {
				String error =
						violation.getRootBeanClass().getName() + " " + violation.getPropertyPath() + ": " + violation
								.getMessage();
				RootCause rootCause = new RootCause(HttpStatus.BAD_REQUEST.value(), error);
				errors.add(rootCause);
			}
			errorData.setError(new Error(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors));
		}
	}

	// Handler to handle all custom exceptions and access denied exception.
	@ExceptionHandler({ Exception.class })
	public ResponseEntity<Object> handleAll(final Exception ex, final WebRequest request) {
		logger.info(ex.getClass().getName());
		logger.error("error", ex);
		ErrorData errorData = new ErrorData(new Error(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage()));
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		if (ex instanceof NoDataFoundException || ex instanceof ValidationException || ex instanceof ServiceException) {
			Error error = ((ServiceException) ex).getError();
			status = error.getStatus();
			if (CollectionUtils.isEmpty(error.getRootCauses())) {
				error.setRootCauses(Arrays.asList(new RootCause(status.value(), ex.getMessage())));
			}
			error.setReason(status.getReasonPhrase());
			errorData.setError(error);
		} else if (ex instanceof AccessDeniedException) {
			errorData.setError(ErrorUtil
					.populateError(HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.getReasonPhrase(),
							Arrays.asList(new RootCause(status.value(), "Not authorized."))));
		} else if (ex instanceof BadSqlGrammarException) {
			errorData.setError(ErrorUtil.populateError(status, status.getReasonPhrase(),
					Arrays.asList(new RootCause(status.value(), ex.getCause().toString()))));
		} else if (ex instanceof ConstraintViolationException) {
			errorData.setError(ErrorUtil
					.populateError(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(),
							Arrays.asList(new RootCause(HttpStatus.BAD_REQUEST.value(), ex.getMessage()))));
		}
		return new ResponseEntity<Object>(errorData, new HttpHeaders(), status);
	}

}
