package com.tmt.shop.entity;

import java.io.Serializable;

/**
 * 订单拆分 管理
 * 
 * @author 超级管理员
 * @date 2017-03-12
 */
public class OrderSplit implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long orders; // 订单
	private Long child; // 子订单

	public Long getOrders() {
		return orders;
	}

	public void setOrders(Long orders) {
		this.orders = orders;
	}

	public Long getChild() {
		return child;
	}

	public void setChild(Long child) {
		this.child = child;
	}
}
