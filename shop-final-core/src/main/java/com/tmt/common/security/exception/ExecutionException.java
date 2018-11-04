package com.tmt.common.security.exception;

import com.tmt.common.exception.BaseRuntimeException;

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