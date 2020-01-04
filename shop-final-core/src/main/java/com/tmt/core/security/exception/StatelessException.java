package com.tmt.core.security.exception;

import com.tmt.core.exception.BaseRuntimeException;

/**
 * 无状态时如果使用了session 则会抛出这个异常
 * 自定义的 session 也不行
 * @author lifeng
 */
public class StatelessException extends BaseRuntimeException {

	private static final long serialVersionUID = 1L;
	
	public StatelessException(String msg) {
		super(msg);
	}
}