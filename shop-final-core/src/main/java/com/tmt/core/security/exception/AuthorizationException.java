package com.tmt.core.security.exception;

import com.tmt.core.exception.BaseRuntimeException;

public class AuthorizationException extends BaseRuntimeException{

	private static final long serialVersionUID = 1L;

	public AuthorizationException() {
		super("验证错误");
	}
	public AuthorizationException(String msg) {
		super(msg);
	}
	public AuthorizationException(int code, String msg) {
		super(code, msg);
	}
	public AuthorizationException(String msg, Throwable cause) {
		super(msg, cause);
	}
	public AuthorizationException(Throwable cause) {
		super(cause);
	}
}
