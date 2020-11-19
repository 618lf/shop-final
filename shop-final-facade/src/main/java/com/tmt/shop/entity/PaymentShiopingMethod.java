package com.tmt.shop.entity;

import java.io.Serializable;

/**
 * 支持的支付方式 管理
 * @author 超级管理员
 * @date 2016-01-20
 */
public class PaymentShiopingMethod implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Long paymentMethods; // 编号
	private Long shippingMethods; // 编号
	private String paymentMethodName; //支付方式名称
	private Byte support;//是否支持0 不支持， 1支持
	
	public Byte getSupport() {
		return support;
	}
	public void setSupport(Byte support) {
		this.support = support;
	}
	public String getPaymentMethodName() {
		return paymentMethodName;
	}
	public void setPaymentMethodName(String paymentMethodName) {
		this.paymentMethodName = paymentMethodName;
	}
	public Long getPaymentMethods() {
		return paymentMethods;
	}
	public void setPaymentMethods(Long paymentMethods) {
		this.paymentMethods = paymentMethods;
	}
    public Long getShippingMethods() {
		return shippingMethods;
	}
	public void setShippingMethods(Long shippingMethods) {
		this.shippingMethods = shippingMethods;
	}
}