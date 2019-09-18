package com.home.services.exception.controller;

import java.util.Arrays;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import com.home.services.model.ErrorData;
import com.home.services.model.RootCause;
import com.home.services.util.ErrorUtil;

/**
 * Default class to return the json errors to the http requests.
 */
@RestController
@AutoConfigureBefore(ErrorMvcAutoConfiguration.class)
public class ServiceErrorController implements ErrorController {
	private final static String ERROR_PATH = "/error";
	private Logger logger = LoggerFactory.getLogger(ServiceErrorController.class);
	private ErrorAttributes errorAttributes;

	@Autowired
	public ServiceErrorController(ErrorAttributes errorAttributes) {
		this.errorAttributes = errorAttributes;
	}

	@Override
	public String getErrorPath() {
		return ERROR_PATH;
	}

	/**
	 * Generates error ResponseEntity
	 *
	 * @param request
	 * @return ResponseEntity<Response>
	 */
	@RequestMapping(value = ERROR_PATH)
	public ResponseEntity<ErrorData> error(HttpServletRequest request) {
		logger.debug(" error method throwing no handler exception");
		HttpStatus httpStatus = getHttpStatus(request);
		ServletWebRequest servletWebRequest = new ServletWebRequest(request);
		Map<String, Object> errorAttributes = this.errorAttributes.getErrorAttributes(servletWebRequest, true);
		String error = (String) errorAttributes.get("error");
		String message = (String) errorAttributes.get("message");
		Integer status = (Integer) errorAttributes.get("status");
		logger.error("message|error|status : " + message + "|" + error + "|" + status);
		return new ResponseEntity<>(new ErrorData(ErrorUtil.populateError(httpStatus, error, Arrays.asList(
				new RootCause(httpStatus.value(), httpStatus.equals(HttpStatus.UNAUTHORIZED) ? message : error)))),
				httpStatus);
	}

	/**
	 * retrieves the http status code from request.
	 *
	 * @param request
	 * @return HttpStatus
	 */
	private HttpStatus getHttpStatus(HttpServletRequest request) {
		Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
		if (statusCode != null) {
			try {
				return HttpStatus.valueOf(statusCode);
			} catch (Exception ex) {
				logger.error(this.getClass().getName(), ", getHttpStatus, exception: " + ex.getMessage());
			}
		}
		return HttpStatus.INTERNAL_SERVER_ERROR;
	}

}
