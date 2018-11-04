package com.tmt.common.security.exception;

import com.tmt.common.exception.BaseRuntimeException;

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