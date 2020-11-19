package com.tmt.shop.entity;

import java.io.Serializable;

/**
 * 下单统计 管理
 * @author 超级管理员
 * @date 2016-12-08
 */
public class UserOrders implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Long userId; // 主键
	private Integer orders; // 订单数量
    
    public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
    public Integer getOrders() {
		return orders;
	}
	public void setOrders(Integer orders) {
		this.orders = orders;
	}
}