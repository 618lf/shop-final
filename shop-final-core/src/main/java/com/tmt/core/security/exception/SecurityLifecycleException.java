package com.tmt.core.security.exception;

import com.tmt.core.exception.BaseRuntimeException;

/**
 * 超出了安全控件的生命周期
 * @author lifeng
 */
public class SecurityLifecycleException extends BaseRuntimeException {

	private static final long serialVersionUID = 1L;
	
	public SecurityLifecycleException(String msg) {
		super(msg);
	}
}