package com.home.services.model;

public class RootCause {

	private int code;
	private String reason;

	public RootCause(final int code, final String reason){
		this.code = code;
		this.reason = reason;
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
}
