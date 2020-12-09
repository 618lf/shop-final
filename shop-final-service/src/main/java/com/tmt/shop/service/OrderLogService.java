package com.tmt.shop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.core.service.BaseService;
import com.tmt.shop.dao.OrderLogDao;
import com.tmt.shop.entity.Order;
import com.tmt.shop.entity.OrderLog;
import com.tmt.system.entity.User;

/**
 * 订单记录 管理
 * @author 超级管理员
 * @date 2016-01-21
 */
@Service("shopOrderLogService")
public class OrderLogService extends BaseService<OrderLog,Long> implements OrderLogServiceFacade{
	
	@Autowired
	private OrderLogDao orderLogDao;
	
	@Override
	protected BaseDaoImpl<OrderLog, Long> getBaseDao() {
		return orderLogDao;
	}
	
	/**
	 * 新建订单
	 * --- 当前用户
	 */
	@Transactional
	public void newOrder(Order order) {
		OrderLog log = new OrderLog();
		log.setOrderId(order.getId());
		log.setContent("订单创建");
		log.setType(OrderLog.Type.create);
		log.userOrder(order);
		this.insert(log);
	}
	
	/**
	 * 审核订单
	 * --- 当前用户
	 */
	@Transactional
	public void confirmOrder(Order order, User user) {
		OrderLog log = new OrderLog();
		log.setOrderId(order.getId());
		log.setContent("订单审核");
		log.setType(OrderLog.Type.confirm);
		if (user != null) {
			log.userOptions(user);
		}
		this.insert(log);
	}
	
	/**
	 * 修改订单
	 * --- 当前用户
	 */
	@Transactional
	public void specialOrder(Order order, User user, String content) {
		OrderLog log = new OrderLog();
		log.setOrderId(order.getId());
		log.setContent(content); 
		log.setType(OrderLog.Type.modify);
		if (user != null) {
			log.userOptions(user);
		}
		this.insert(log);
	}
	
	/**
	 * 改价订单
	 * --- 当前用户
	 */
	@Transactional
	public void mdamountOrder(Order order, User user, String content) {
		OrderLog log = new OrderLog();
		log.setOrderId(order.getId());
		log.setContent(content);
		log.setType(OrderLog.Type.modify);
		if (user != null) {
			log.userOptions(user);
		}
		this.insert(log);
	}
	
	/**
	 * 改发票订单
	 * --- 当前用户
	 */
	@Transactional
	public void mdinvoiceOrder(Order order, User user, String content) {
		OrderLog log = new OrderLog();
		log.setOrderId(order.getId());
		log.setContent(content);
		log.setType(OrderLog.Type.modify);
		if (user != null) {
			log.userOptions(user);
		}
		this.insert(log);
	}
	
	/**
	 * 改发货订单
	 * --- 当前用户
	 */
	@Transactional
	public void mdshippingOrder(Order order, User user, String content) {
		OrderLog log = new OrderLog();
		log.setOrderId(order.getId());
		log.setContent(content);
		log.setType(OrderLog.Type.modify);
		if (user != null) {
			log.userOptions(user);
		}
		this.insert(log);
	}
	
	/**
	 * 发货订单
	 * --- 当前用户
	 */
	@Transactional
	public void shippingOrder(Order order, User user, String content) {
		OrderLog log = new OrderLog();
		log.setOrderId(order.getId());
		log.setContent(content);
		log.setType(OrderLog.Type.shipping);
		if (user != null) {
			log.userOptions(user);
		}
		this.insert(log);
	}
	
	/**
	 * 取消发货订单
	 * --- 当前用户
	 */
	@Transactional
	public void unShippingOrder(Order order, User user, String content) {
		OrderLog log = new OrderLog();
		log.setOrderId(order.getId());
		log.setContent(content);
		log.setType(OrderLog.Type.unshipping);
		if (user != null) {
			log.userOptions(user);
		}
		this.insert(log);
	}
	
	/**
	 * 确认收货
	 * --- 当前用户
	 */
	@Transactional
	public void receiptOrder(Order order, User user) {
		OrderLog log = new OrderLog();
		log.setOrderId(order.getId());
		log.setContent("订单已确认收货");
		log.setType(OrderLog.Type.receipt);
		if (user != null) {
			log.userOptions(user);
		} else {
			log.userOrder(order);
		}
		this.insert(log);
	}
	
	/**
	 * 申请退货
	 * --- 当前用户
	 */
	@Transactional
	public void applyReturnsOrder(Order order) {
		OrderLog log = new OrderLog();
		log.setOrderId(order.getId());
		log.setContent("订单已申请退货");
		log.setType(OrderLog.Type.apply_returns);
		log.userOrder(order);
		this.insert(log);
	}
	
	/**
	 * 申请退货
	 * --- 当前用户
	 */
	@Transactional
	public void applyRefundOrder(Order order) {
		OrderLog log = new OrderLog();
		log.setOrderId(order.getId());
		log.setContent("订单已申请退款");
		log.setType(OrderLog.Type.apply_refund);
		log.userOrder(order);
		this.insert(log);
	}
	
	/**
	 * 付款订单
	 * --- 当前用户
	 */
	@Transactional
	public void paymentOrder(Order order, User user) {
		OrderLog log = new OrderLog();
		log.setOrderId(order.getId());
		log.setContent(new StringBuilder("订单支付，支付方式：").append(order.getPaymentMethodName()).toString()); 
		log.setType(OrderLog.Type.payment);
		if (user != null) {
			log.userOptions(user);
		} else {
			log.userOrder(order);
		}
		this.insert(log);
	}
	
	/**
	 * 订单完成
	 * --- 当前用户
	 */
	@Transactional
	public void completeOrder(Order order, User user) {
		OrderLog log = new OrderLog();
		log.setOrderId(order.getId());
		log.setContent("订单完成");
		log.setType(OrderLog.Type.complete);
		if (user != null) {
			log.userOptions(user);
		}
		this.insert(log);
	}
	
	
	/**
	 * 订单取消
	 * --- 当前用户
	 */
	@Transactional
	public void cancelOrder(Order order, User user) {
		OrderLog log = new OrderLog();
		log.setOrderId(order.getId());
		log.setContent("订单取消");
		log.setType(OrderLog.Type.cancel);
		if (user != null) {
			log.userOptions(user);
		}
		this.insert(log);
	}
	
	/**
	 * 退货
	 * --- 当前用户
	 */
	@Transactional
	public void returnsOrder(Order order, User user, String content) {
		OrderLog log = new OrderLog();
		log.setOrderId(order.getId());
		log.setContent(content);
		log.setType(OrderLog.Type.returns);
		if (user != null) {
			log.userOptions(user);
		}
		this.insert(log);
	}
	
	/**
	 * 退款
	 * --- 当前用户
	 */
	@Transactional
	public void processRefoundsOrder(Order order, User user, String content) {
		OrderLog log = new OrderLog();
		log.setOrderId(order.getId());
		log.setContent(content);
		log.setType(OrderLog.Type.refunds_process);
		if (user != null) {
			log.userOptions(user);
		}
		this.insert(log);
	}
	
	/**
	 * 退款
	 * --- 当前用户
	 */
	@Transactional
	public void refoundsOrder(Order order, User user, String content) {
		OrderLog log = new OrderLog();
		log.setOrderId(order.getId());
		log.setContent(content);
		log.setType(OrderLog.Type.refunds);
		if (user != null) {
			log.userOptions(user);
		}
		this.insert(log);
	}
}