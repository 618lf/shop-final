package com.tmt.common.web.security.exception;

import com.tmt.common.exception.ErrorCode;
import com.tmt.common.security.exception.AuthenticationException;

/**
 * 用户名密码错误
 * @author lifeng
 */
public class UsernamePasswordException extends AuthenticationException{

	private static final long serialVersionUID = -292585289627016645L;

	public UsernamePasswordException(ErrorCode code) {
		super(code);
	}
}