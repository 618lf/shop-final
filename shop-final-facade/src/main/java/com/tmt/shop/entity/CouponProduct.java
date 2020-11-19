package com.tmt.shop.entity;

import java.io.Serializable;
/**
 * 优惠商品 管理
 * @author 超级管理员
 * @date 2016-11-26
 */
public class CouponProduct implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Long coupons; // 优惠券
	private Long products; // 商品
	private Product product; // 具体的商品信息
	
    public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public Long getCoupons() {
		return coupons;
	}
	public void setCoupons(Long coupons) {
		this.coupons = coupons;
	}
    public Long getProducts() {
		return products;
	}
	public void setProducts(Long products) {
		this.products = products;
	}
}