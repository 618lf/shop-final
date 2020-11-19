package com.tmt.shop.entity;

import java.io.Serializable;

import com.tmt.core.entity.IdEntity;

/**
 * 促销优惠券 管理
 * 
 * @author 超级管理员
 * @date 2016-11-26
 */
public class PromotionCoupon extends IdEntity<Long> implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long promotions; // 促销
	private Long coupons; // 优惠
	private Integer val; // 面值
	private Coupon coupon; // 具体的优惠

	public Integer getVal() {
		return val;
	}

	public void setVal(Integer val) {
		this.val = val;
	}

	public Coupon getCoupon() {
		return coupon;
	}

	public void setCoupon(Coupon coupon) {
		this.coupon = coupon;
	}

	public Long getPromotions() {
		return promotions;
	}

	public void setPromotions(Long promotions) {
		this.promotions = promotions;
	}

	public Long getCoupons() {
		return coupons;
	}

	public void setCoupons(Long coupons) {
		this.coupons = coupons;
	}
}