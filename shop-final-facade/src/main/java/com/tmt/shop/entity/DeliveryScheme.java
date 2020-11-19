package com.tmt.shop.entity;

import java.io.Serializable;

import com.tmt.core.entity.BaseEntity;
/**
 * 配送方案 管理
 * @author 超级管理员
 * @date 2016-11-06
 */
public class DeliveryScheme extends BaseEntity<Long> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String name; // 名称
	private Byte isDefault; // 是否默认
	private String shippingExpression; // 发货表达式
	private String deliveryExpression; // 配送表达式
    
    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
    
    public Byte getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(Byte isDefault) {
		this.isDefault = isDefault;
	}
    
    public String getShippingExpression() {
		return shippingExpression;
	}
	public void setShippingExpression(String shippingExpression) {
		this.shippingExpression = shippingExpression;
	}
    
    public String getDeliveryExpression() {
		return deliveryExpression;
	}
	public void setDeliveryExpression(String deliveryExpression) {
		this.deliveryExpression = deliveryExpression;
	}
}
