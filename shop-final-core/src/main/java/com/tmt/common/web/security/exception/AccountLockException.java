package com.tmt.common.web.security.exception;

import com.tmt.common.exception.ErrorCode;
import com.tmt.common.security.exception.AuthenticationException;

/**
 * 账户锁定异常
 * @author lifeng
 */
public class AccountLockException extends AuthenticationException {

	private static final long serialVersionUID = 4579680236680135972L;

	public AccountLockException(ErrorCode code) {
		super(code);
	}
}