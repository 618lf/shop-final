package com.tmt.shop.service;

import java.util.List;

import com.tmt.core.service.BaseServiceFacade;
import com.tmt.shop.entity.Epay;
import com.tmt.shop.entity.Order;
import com.tmt.shop.entity.Payment;
import com.tmt.shop.entity.Rank;
import com.tmt.system.entity.User;

/**
 * 收款 管理
 * @author 超级管理员
 * @date 2015-11-05
 */
public interface PaymentServiceFacade extends BaseServiceFacade<Payment,Long> {
	
	/**
	 * 订单的支付信息
	 * @param orderId
	 * @return
	 */
	public List<Payment> queryPaymentsByOrderId(Long orderId);
	
	/**
	 * 初始化一个支付
	 * @return
	 */
	public Payment init(Payment payment, User user);
	
	
	/**
	 * 修改支付插件类型
	 * @param order
	 */
	public void updateEpay(Payment payment);
	
	/**
	 * 确认支付
	 * @param payment
	 */
	public void confirmPay(Payment payment);
	
	/**
	 * 手动
	 * @param payment
	 * @param user
	 */
	public void manualPayment(Payment payment, User user);
	
	/**
	 * 初始化支付 -- 订单
	 * @param order
	 * @param user
	 * @return
	 */
	public Payment initPayment(Epay epay, Order order, User user);
	
	/**
	 * 初始化支付 -- 等级
	 * @param frank
	 * @param trank
	 * @param user
	 * @return
	 */
	public Payment initPayment(Epay epay, Rank frank, Rank trank, User user);
}
