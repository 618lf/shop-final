package com.tmt.shop.coupon;

import com.tmt.shop.entity.Coupon;
import com.tmt.shop.entity.Order;

/**
 * 优惠券处理
 * @author root
 */
public interface CouponHandler {

	/**
	 * 主要验证此优惠券的有效性
	 * @param code
	 */
	public boolean doHandler(Coupon coupon, Order order);
}
