package com.tmt.shop.entity;

import java.io.Serializable;

/**
 * 部分数据
 * @author root
 *
 */
public class CouponMini implements Serializable{

	private static final long serialVersionUID = 1L;

	private Long coupon; // 优惠券
	private Long couponCode; // 优惠券
	public Long getCoupon() {
		return coupon;
	}
	public void setCoupon(Long coupon) {
		this.coupon = coupon;
	}
	public Long getCouponCode() {
		return couponCode;
	}
	public void setCouponCode(Long couponCode) {
		this.couponCode = couponCode;
	}
}