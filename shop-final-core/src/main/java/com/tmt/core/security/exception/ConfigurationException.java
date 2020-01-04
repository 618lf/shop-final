package com.tmt.core.security.exception;

import com.tmt.core.exception.BaseRuntimeException;

public class ConfigurationException extends BaseRuntimeException{

	private static final long serialVersionUID = 1L;
	
	public ConfigurationException(int code, String msg) {
		super(code, msg);
	}

	public ConfigurationException(String msg) {
		super(msg);
	}

	public ConfigurationException(Throwable cause) {
		super(cause);
	}

	public ConfigurationException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
