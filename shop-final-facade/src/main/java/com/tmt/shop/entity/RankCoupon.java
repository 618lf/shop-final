package com.tmt.shop.entity;

import java.io.Serializable;

import com.tmt.core.entity.IdEntity;

/**
 * 等级赠送优惠券 管理
 * 
 * @author 超级管理员
 * @date 2017-05-11
 */
public class RankCoupon extends IdEntity<Long> implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long ranks; // 等级
	private Long coupons; // 优惠
	private Coupon coupon;

	public Coupon getCoupon() {
		return coupon;
	}

	public void setCoupon(Coupon coupon) {
		this.coupon = coupon;
	}

	public Long getRanks() {
		return ranks;
	}

	public void setRanks(Long ranks) {
		this.ranks = ranks;
	}

	public Long getCoupons() {
		return coupons;
	}

	public void setCoupons(Long coupons) {
		this.coupons = coupons;
	}
}