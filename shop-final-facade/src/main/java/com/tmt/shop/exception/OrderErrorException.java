package com.tmt.shop.exception;

import com.tmt.core.exception.BaseRuntimeException;

/**
 * 订单异常
 * 
 * @author root
 */
public class OrderErrorException extends BaseRuntimeException {

	private static final long serialVersionUID = 1L;

	public OrderErrorException(String msg) {
		super(msg);
	}
}
