package com.tmt.shop.entity;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;
import com.tmt.core.entity.BaseEntity;
import com.tmt.core.utils.BigDecimalUtil;

/**
 * 收款 管理
 * 
 * @author 超级管理员
 * @date 2015-11-05
 */
public class Payment extends BaseEntity<Long> implements Serializable, Payable {

	private static final long serialVersionUID = 1L;
	public static final byte order_module = 1;
	public static final byte rank_module = 2;

	private String sn; // 唯一序列号
	private String transactionId; // 支付的业务ID
	private Long epayId; // 支付插件
	private Long orderId; // 订单 -- 支付实体ID
	private java.math.BigDecimal amount; // 支付金额
	private java.util.Date expire; // 过期时间
	private String payer; // 付款人
	private java.util.Date paymentDate; // 付款日期
	private String payBody; // 支付描述
	private Byte payFlag; // 支付状态：0未支付， 1已支付
	private Byte module; // 模块
	private String account; // 收款账户(针对银行汇款的方式)

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public Byte getModule() {
		return module;
	}

	public void setModule(Byte module) {
		this.module = module;
	}

	public void setPayBody(String payBody) {
		this.payBody = payBody;
	}

	public String getPayBody() {
		return payBody;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

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

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public java.math.BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(java.math.BigDecimal amount) {
		this.amount = amount;
	}

	public java.util.Date getExpire() {
		return expire;
	}

	public void setExpire(java.util.Date expire) {
		this.expire = expire;
	}

	public String getPayer() {
		return payer;
	}

	public void setPayer(String payer) {
		this.payer = payer;
	}

	public java.util.Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(java.util.Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	public Byte getPayFlag() {
		return payFlag;
	}

	public void setPayFlag(Byte payFlag) {
		this.payFlag = payFlag;
	}

	// 支付相关的参数
	@JSONField(serialize = false)
	@Override
	public String getJsSn() {
		return new StringBuilder(ShopConstant.JS_PAY_PREFIX).append(this.getId()).toString();
	}

	@JSONField(serialize = false)
	@Override
	public String getNaSn() {
		return new StringBuilder(ShopConstant.NA_PAY_PREFIX).append(this.getId()).toString();
	}

	@JSONField(serialize = false)
	@Override
	public String getPayPrice() {
		return BigDecimalUtil.toString(BigDecimalUtil.mul(this.getAmount(), BigDecimalUtil.valueOf(100)), 0);
	}
}