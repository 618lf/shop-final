package com.tmt.shop.entity;

import java.io.Serializable;

import com.tmt.core.entity.BaseEntity;

/**
 * 企业支付安全设置 管理
 * @author 超级管理员
 * @date 2015-12-21
 */
public class EpaySafe extends BaseEntity<Long> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private java.math.BigDecimal singleOrderMaxAmount; // 单笔最大金额
	private java.math.BigDecimal singleUserMaxAmount; // 每天每个用户提现最大金额
	private String manager; // 支付管理员
	
    
    public java.math.BigDecimal getSingleOrderMaxAmount() {
		return singleOrderMaxAmount;
	}
	public void setSingleOrderMaxAmount(java.math.BigDecimal singleOrderMaxAmount) {
		this.singleOrderMaxAmount = singleOrderMaxAmount;
	}
    
    public java.math.BigDecimal getSingleUserMaxAmount() {
		return singleUserMaxAmount;
	}
	public void setSingleUserMaxAmount(java.math.BigDecimal singleUserMaxAmount) {
		this.singleUserMaxAmount = singleUserMaxAmount;
	}
    
    public String getManager() {
		return manager;
	}
	public void setManager(String manager) {
		this.manager = manager;
	}
	
	@Override
	public Long prePersist() {
		return this.getId();
	}
}
