package com.tmt.shop.entity;

import java.io.Serializable;
/**
 * 商品发货方案 管理
 * @author 超级管理员
 * @date 2016-11-06
 */
public class GoodsDelivery implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Long goodsId; // 商品
	private Long deliveryId; // 发货
	private String name; // 名称
	private String shippingExpression; // 发货表达式
	private String deliveryExpression; // 配送表达式
    
    public Long getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}
    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public Long getDeliveryId() {
		return deliveryId;
	}
	public void setDeliveryId(Long deliveryId) {
		this.deliveryId = deliveryId;
	}
}