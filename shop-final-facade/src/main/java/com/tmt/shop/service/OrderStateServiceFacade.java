package com.tmt.shop.service;

import java.util.List;

import com.tmt.core.entity.LabelVO;
import com.tmt.core.service.BaseServiceFacade;
import com.tmt.shop.entity.Order;
import com.tmt.shop.entity.OrderState;
import com.tmt.shop.entity.Shipping;

/**
 * 订单状态 管理
 * @author 超级管理员
 * @date 2016-10-06
 */
public interface OrderStateServiceFacade extends BaseServiceFacade<OrderState,Long> {
	
	
	/**
	 * 延迟执行
	 */
	public void expire(OrderState state);
	
	/**
	 * 置为可执行
	 */
	public void enabled(List<OrderState> orderStates);
	
	/**
	 * 保存
	 */
	public void save(OrderState orderState);
	
    /**
     * 获取前几个 (会更新状态)
     * @param qc
     * @param size   
     * @return
     */
    public List<OrderState> queryUpdateAbles(int size);
	
	
	/**
	 * 下单
	 * @param order
	 */
	public void book(Order order);
	
	/**
	 * 审核
	 * @param order
	 */
	public void confirm(Order order);
	
	/**
	 * 付款
	 * @param order
	 */
	public void pay(Order order);
	
	/**
	 * 发货
	 * @param order
	 */
	public void shipping(Order order, Shipping shipping);
	
	/**
	 * 取消发货-标记已处理的付款状态
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
	
	/**
	 * 取消
	 */
	public void cancel(Order order);
	
	/**
	 * 完成
	 */
	public void complete(Order order);
	
	/**
	 * 实时状态
	 */
	public List<LabelVO> statOrders();
}