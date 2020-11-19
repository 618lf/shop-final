package com.tmt.shop.entity;

import java.io.Serializable;
/**
 * 订单优惠 管理
 * @author 超级管理员
 * @date 2016-07-13
 */
public class OrderCoupon implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Long orders; // 订单ID
	private Long coupons; // 优惠码
	private String couponName; // 优惠名称
	private Integer val;
    
    public String getCouponName() {
		return couponName;
	}
	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}
	public Integer getVal() {
		return val;
	}
	public void setVal(Integer val) {
		this.val = val;
	}
	public Long getOrders() {
		return orders;
	}
	public void setOrders(Long orders) {
		this.orders = orders;
	}
    public Long getCoupons() {
		return coupons;
	}
	public void setCoupons(Long coupons) {
		this.coupons = coupons;
	}
}
