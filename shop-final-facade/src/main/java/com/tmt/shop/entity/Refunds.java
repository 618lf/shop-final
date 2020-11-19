package com.tmt.shop.entity;

import java.io.Serializable;

import com.tmt.core.entity.BaseEntity;
import com.tmt.core.utils.BigDecimalUtil;

/**
 * 退款管理 管理
 * 
 * @author 超级管理员
 * @date 2016-01-21
 */
public class Refunds extends BaseEntity<Long> implements Serializable {

	private static final long serialVersionUID = 1L;

	private String sn; // 序列号
	private String transactionId; // 支付的业务ID --- 支付相关的
	private Byte type; // 退款方式 0： 网上支付的原路退回， 1：线下退款
	private Long epayId; // 支付插件
	private Long orderId; // 编号
	private String orderSn; // 订单编号
	private String account; // 付款账户
	private java.math.BigDecimal amount; // 支付金额
	private String payee; // 收款人
	private Byte state; // 状态0 未提交申请， 1先提交申请，在查询退款状态， 2 处理完成

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public Long getEpayId() {
		return epayId;
	}

	public void setEpayId(Long epayId) {
		this.epayId = epayId;
	}

	public Byte getType() {
		return type;
	}

	public void setType(Byte type) {
		this.type = type;
	}

	public Byte getState() {
		return state;
	}

	public void setState(Byte state) {
		this.state = state;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getOrderSn() {
		return orderSn;
	}

	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public java.math.BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(java.math.BigDecimal amount) {
		this.amount = amount;
	}

	public String getPayee() {
		return payee;
	}

	public void setPayee(String payee) {
		this.payee = payee;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public Refunds copy() {
		Refunds newRefunds = new Refunds();
		newRefunds.setOrderId(this.getOrderId());
		newRefunds.setOrderSn(this.getOrderSn());
		return newRefunds;
	}

	public String getPayPrice() {
		return BigDecimalUtil.toString(BigDecimalUtil.mul(this.getAmount(), BigDecimalUtil.valueOf(100)), 0);
	}
}