package com.home.services.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorData {
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonProperty("errors")
	private Error error;

	public ErrorData(final Error error){
		this.error = error;
	}

	public Error getError() {
		return error;
	}

	public void setError(Error error) {
		this.error = error;
	}
}
