package com.tmt.shop.service;

import java.util.List;

import com.tmt.core.service.BaseServiceFacade;
import com.tmt.shop.entity.Order;
import com.tmt.shop.entity.OrderItem;

/**
 * 订单明细 管理
 * @author 超级管理员
 * @date 2015-09-17
 */
public interface OrderItemServiceFacade extends BaseServiceFacade<OrderItem,Long> {
	
	/**
	 * 保存
	 */
	public void save(Order order);
	
	/**
	 * 查询订单的订单明细
	 * @param orderId
	 * @return
	 */
	public List<OrderItem> queryItemsByOrderId(Long orderId);
	
	/**
	 * 删除
	 * @param items
	 */
	public void delete(List<OrderItem> items);
	
	/**
	 * 发货
	 * @param items
	 */
	public void updateShipping(List<OrderItem> items);
	
	/**
	 * 退货
	 * @param items
	 */
	public void updateReturn(List<OrderItem> items);
}