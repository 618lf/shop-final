package com.tmt.shop.service;

import com.tmt.core.service.BaseServiceFacade;
import com.tmt.shop.entity.Order;
import com.tmt.shop.entity.OrderLog;
import com.tmt.system.entity.User;

/**
 * 订单记录 管理
 * @author 超级管理员
 * @date 2016-01-21
 */
public interface OrderLogServiceFacade extends BaseServiceFacade<OrderLog,Long> {
	
	/**
	 * 新建订单
	 */
	public void newOrder(Order order);
	
	/**
	 * 审核订单
	 */
	public void confirmOrder(Order order, User user);
	
	/**
	 * 特殊处理订单
	 */
	public void specialOrder(Order order, User user, String content);
	
	/**
	 * 改价订单
	 */
	public void mdamountOrder(Order order, User user, String content);
	
	/**
	 * 改发票订单
	 */
	public void mdinvoiceOrder(Order order, User user, String content);
	
	/**
	 * 改发货订单
	 */
	public void mdshippingOrder(Order order, User user, String content);
	
	/**
	 * 发货订单
	 */
	public void shippingOrder(Order order, User user, String content);
	
	/**
	 * 确认收货
	 */
	public void receiptOrder(Order order, User user);
	
	/**
	 * 付款订单
	 */
	public void paymentOrder(Order order, User user);
	
	/**
	 * 订单完成
	 */
	public void completeOrder(Order order, User user);
	
	
	/**
	 * 订单取消
	 */
	public void cancelOrder(Order order, User user);
	
	/**
	 * 退货
	 */
	public void returnsOrder(Order order, User user, String content);
	
	/**
	 * 退款
	 */
	public void refoundsOrder(Order order, User user, String content);
}