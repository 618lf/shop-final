package com.tmt.shop.service;

import java.util.List;

import com.tmt.shop.entity.Order;

/**
 * 订单拆分 管理
 * @author 超级管理员
 * @date 2017-03-12
 */
public interface OrderSplitServiceFacade {
	
	/**
	 * 拆分
	 * @param master
	 * @param orders
	 */
	public Boolean split(Order master, List<Order> orders);
}