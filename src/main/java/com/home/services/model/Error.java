package com.home.services.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import org.springframework.http.HttpStatus;

public class Error {
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonProperty("root_cause")
	private List<RootCause> rootCauses;

	@JsonInclude(JsonInclude.Include.NON_DEFAULT)
	private int code;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String reason;

	@JsonInclude(JsonInclude.Include.NON_DEFAULT)
	private long timestamp;

	@JsonIgnore
	private HttpStatus status;

	public Error() {
		super();
	}

	public Error(final HttpStatus status, final String reason){
		super();
		this.code = status.value();
		this.status = status;
		this.reason = reason;
	}

	public Error(final HttpStatus status, final String reason, final List<RootCause> errors) {
		super();
		this.code = status.value();
		this.status = status;
		this.reason = reason;
		this.rootCauses = errors;
	}

	public List<RootCause> getRootCauses() {
		return rootCauses;
	}

	public void setRootCauses(List<RootCause> rootCauses) {
		this.rootCauses = rootCauses;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

}
