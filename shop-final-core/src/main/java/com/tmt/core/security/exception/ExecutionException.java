package com.tmt.core.security.exception;

import com.tmt.core.exception.BaseRuntimeException;

public class ExecutionException extends BaseRuntimeException {

	private static final long serialVersionUID = 1L;
	
	public ExecutionException(int code, String msg) {
		super(code, msg);
	}

	public ExecutionException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public ExecutionException(Throwable cause) {
		super(cause);
	}
}