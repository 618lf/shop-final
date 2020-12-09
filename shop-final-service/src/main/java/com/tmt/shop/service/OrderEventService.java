package com.tmt.shop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.shop.dao.OrderEventDao;
import com.tmt.shop.entity.Order;
import com.tmt.shop.entity.OrderEvent;
import com.tmt.shop.entity.OrderOpts;

/**
 * 订单事件 管理
 * @author 超级管理员
 * @date 2016-10-04
 */
@Service("shopOrderEventService")
public class OrderEventService implements OrderEventServiceFacade{
	
	@Autowired
	private OrderEventDao orderEventDao;
	
	/**
	 * 删除
	 */
	@Override
	@Transactional
	public void delete(List<OrderEvent> orderEvents) {
		this.orderEventDao.batchDelete(orderEvents);
	}
	
    /**
     * 获取前几个 (会更新状态)
     * @param qc
     * @param size   
     * @return
     */
    @Override
    @Transactional
    public List<OrderEvent> queryUpdateAbles(int size) {
    	return orderEventDao.queryUpdateAbles(size);
    }
	
	/**
	 * 下单
	 * @param order
	 */
    @Override
	@Transactional
	public void book(Order order) {
		OrderEvent event = new OrderEvent();
		event.setOrderId(order.getId());
		event.setOpt(OrderOpts.BOOK);
		event.setState((byte)0);
		this.orderEventDao.insert(event);
	}
	
	/**
	 * 付款
	 * @param order
	 */
    @Override
	@Transactional
	public void pay(Order order) {
		OrderEvent event = new OrderEvent();
		event.setOrderId(order.getId());
		event.setOpt(OrderOpts.PAY);
		event.setState((byte)0);
		this.orderEventDao.insert(event);
	}
	
	/**
	 * 发货
	 * @param order
	 */
    @Override
	@Transactional
	public void shipping(Order order) {
		OrderEvent event = new OrderEvent();
		event.setOrderId(order.getId());
		event.setOpt(OrderOpts.SHIPPING);
		event.setState((byte)0);
		this.orderEventDao.insert(event);
	}
    
	/**
	 * 取消发货
	 * @param order
	 */
    @Override
	@Transactional
	public void unshipping(Order order) {}
	
	/**
	 * 收货
	 * @param order
	 */
    @Override
	@Transactional
	public void receipt(Order order) {
		OrderEvent event = new OrderEvent();
		event.setOrderId(order.getId());
		event.setOpt(OrderOpts.RECEIPT);
		event.setState((byte)0);
		this.orderEventDao.insert(event);
	}
    
	/**
	 * 申请 - 退货
	 * @param order
	 */
    @Override
	@Transactional
	public void applyReturns(Order order) {
    	OrderEvent event = new OrderEvent();
		event.setOrderId(order.getId());
		event.setOpt(OrderOpts.APPLY_RETURNS);
		event.setState((byte)0);
		this.orderEventDao.insert(event);
	}
    
	/**
	 * 退货
	 * @param order
	 */
    @Override
	@Transactional
	public void returns(Order order) {
		OrderEvent event = new OrderEvent();
		event.setOrderId(order.getId());
		event.setOpt(OrderOpts.RETURNS);
		event.setState((byte)0);
		this.orderEventDao.insert(event);
	}
    
    /**
	 * 申请 - 退款
	 * @param order
	 */
    @Override
	@Transactional
	public void applyRefund(Order order) {
    	OrderEvent event = new OrderEvent();
		event.setOrderId(order.getId());
		event.setOpt(OrderOpts.APPLY_REFUND);
		event.setState((byte)0);
		this.orderEventDao.insert(event);
	}
    
	/**
	 * 退款 - 处理
	 * @param order
	 */
    @Override
	@Transactional
	public void refundsProcess(Order order) {
		OrderEvent event = new OrderEvent();
		event.setOrderId(order.getId());
		event.setOpt(OrderOpts.REFUND_PROCESS);
		event.setState((byte)0);
		this.orderEventDao.insert(event);
	}
	
	/**
	 * 退款
	 * @param order
	 */
    @Override
	@Transactional
	public void refunds(Order order) {
		OrderEvent event = new OrderEvent();
		event.setOrderId(order.getId());
		event.setOpt(OrderOpts.REFUNDS);
		event.setState((byte)0);
		this.orderEventDao.insert(event);
	}
}