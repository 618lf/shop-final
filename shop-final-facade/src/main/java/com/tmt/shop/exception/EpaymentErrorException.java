package com.tmt.shop.exception;

import com.tmt.common.exception.BaseRuntimeException;

/**
 * 订单异常
 * @author root
 *
 */
public class EpaymentErrorException extends BaseRuntimeException{

	private static final long serialVersionUID = 1L;
	
	public EpaymentErrorException(String msg) {
		super(msg);
	}
}
