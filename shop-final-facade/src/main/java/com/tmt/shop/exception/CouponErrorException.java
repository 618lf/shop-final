package com.tmt.shop.exception;

import com.tmt.common.exception.BaseRuntimeException;

/**
 * 优惠券使用异常
 * @author root
 */
public class CouponErrorException extends BaseRuntimeException{

	private static final long serialVersionUID = 1L;
	
	public CouponErrorException(String msg) {
		super(msg);
	}
}