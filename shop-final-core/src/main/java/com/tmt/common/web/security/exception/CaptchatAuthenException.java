package com.tmt.common.web.security.exception;

import com.tmt.common.exception.ErrorCode;
import com.tmt.common.security.exception.AuthenticationException;

/**
 * 验证认证失败
 * @author lifeng
 */
public class CaptchatAuthenException extends AuthenticationException{

	private static final long serialVersionUID = -7858957620929542004L;

	public CaptchatAuthenException(ErrorCode code) {
		super(code);
	}
}