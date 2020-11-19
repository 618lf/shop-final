package com.tmt.shop.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 订单拆分
 * @author lifeng
 */
public class OrderSplits implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long orderId; // 原始订单
	private List<Order> orders; // 拆分后的订单
	
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public List<Order> getOrders() {
		return orders;
	}
	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}
}