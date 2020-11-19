package com.tmt.shop.service;

import java.util.List;

import com.tmt.shop.entity.Order;
import com.tmt.shop.entity.OrderEvent;

/**
 * 订单事件 管理
 * @author 超级管理员
 * @date 2016-10-04
 */
public interface OrderEventServiceFacade {
	
	/**
	 * 删除
	 */
	public void delete(List<OrderEvent> orderEvents);
	
    /**
     * 获取前几个 (会更新状态)
     * @param qc
     * @param size   
     * @return
     */
    public List<OrderEvent> queryUpdateAbles(int size);
	
	/**
	 * 下单
	 * @param order
	 */
	public void book(Order order);
	
	/**
	 * 付款
	 * @param order
	 */
	public void pay(Order order);
	
	/**
	 * 发货
	 * @param order
	 */
	public void shipping(Order order);
	
	/**
	 * 取消发货
	 * @param order
	 */
	public void unshipping(Order order);
	
	/**
	 * 收货
	 * @param order
	 */
	public void receipt(Order order);
	
	/**
	 * 申请 - 退货
	 * @param order
	 */
	public void applyReturns(Order order);
	
	/**
	 * 退货
	 * @param order
	 */
	public void returns(Order order);
	
	/**
	 * 申请 - 退款
	 * @param order
	 */
	public void applyRefund(Order order);
	
	/**
	 * 退款 - 处理
	 * @param order
	 */
	public void refundsProcess(Order order);
	
	/**
	 * 退款
	 * @param order
	 */
	public void refunds(Order order);
}