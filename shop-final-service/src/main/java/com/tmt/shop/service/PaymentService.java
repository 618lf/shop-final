package com.tmt.shop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.core.service.BaseService;
import com.tmt.core.utils.time.DateUtils;
import com.tmt.shop.dao.PaymentDao;
import com.tmt.shop.entity.Epay;
import com.tmt.shop.entity.Order;
import com.tmt.shop.entity.Payment;
import com.tmt.shop.entity.Rank;
import com.tmt.shop.entity.UserRank;
import com.tmt.shop.utils.SnUtils;
import com.tmt.system.entity.User;

/**
 * 收款 管理
 * @author 超级管理员
 * @date 2015-11-05
 */
@Service("shopPaymentService")
public class PaymentService extends BaseService<Payment,Long> implements PaymentServiceFacade{
	
	@Autowired
	private PaymentDao paymentDao;
	@Autowired
	private OrderService orderService;
	@Autowired
	private UserRankService userRankService;
	
	@Override
	protected BaseDaoImpl<Payment, Long> getBaseDao() {
		return paymentDao;
	}
	
	/**
	 * 订单的支付信息
	 * @param orderId
	 * @return
	 */
	public List<Payment> queryPaymentsByOrderId(Long orderId) {
		return this.queryForList("queryPaymentsByOrderId", orderId);
	}
	
	/**
	 * 初始化支付
	 */
	@Override
	public Payment init(Payment payment, User user) {
		payment.setSn(SnUtils.createPaymentSn());
		payment.setPayFlag(Payment.NO);
		payment.userOptions(user);
		this.insert(payment);
		return payment;
	}
	
	@Override
	@Transactional
	public void updateEpay(Payment payment) {
		this.update("updateEpay", payment);
	}

	/**
	 * 确认支付 -- 自动
	 */
	@Override
	@Transactional
	public void confirmPay(Payment payment) {
		
		// 锁定记录
		Payment _payment = this.queryForObject("lock", payment.getId());
		
		// 如果是已支付则不处理
		if (!(_payment != null && _payment.getPayFlag() != null && _payment.getPayFlag() == Payment.NO)) { 
			return;
		}
		
		// 实际的业务
		boolean flag = false;
		if (payment.getModule() == Payment.order_module) {
			flag = this.orderService.autoPayment(payment);
		} else if(payment.getModule() == Payment.rank_module) {
			flag = this.userRankService.confirmPay(payment);
		}
		
		// 保存支付记录
		if (flag) {
			payment.setPayFlag(Payment.YES);
			this.update(payment);
		}
	}
	
	/**
	 * 手动支付
	 * @param payment
	 */
	@Override
	@Transactional
	public void manualPayment(Payment payment, User user) {
		boolean flag = false;
		if (payment.getModule() == Payment.order_module) {
			flag = this.orderService.manualPayment(payment, user);
		}
		
		// 操作成功才包成支付记录
		if (flag) {
			payment.setPayFlag(Payment.YES);
			payment.setPaymentDate(DateUtils.getTimeStampNow());
			payment.setSn(SnUtils.createPaymentSn());
			this.insert(payment);
		}
	}
	
	//  ------------ 业务方法 ---------------
	
	/**
	 * 初始化支付
	 */
	@Override
	@Transactional
	public Payment initPayment(Epay epay, Order order, User user) {
		Payment payment = new Payment();
		payment.setEpayId(epay.getId());
		payment.setModule(Payment.order_module);
		payment.setOrderId(order.getId());
		payment.setPayBody(order.getOrderDesc());
		payment.setAmount(order.getAnountPayAble());
		payment.setPayFlag(Rank.NO);
		payment.userOptions(user);
		return this.init(payment, user);
	}
	
	/**
	 * 购买会员时，申请一个支付订单
	 */
	@Override
	@Transactional
	public Payment initPayment(Epay epay, Rank frank, Rank trank, User user) {
		// 用户等级
		UserRank urank = this.userRankService.getRankandLock(user.getId());
		
		// 是否有变化,可以用户已经购买
		if (urank != null && urank.getRankId() != null && frank != null && urank.getRankId().compareTo(frank.getId()) != 0) {
			return null;
		}
		
		// 初始化
		Payment payment = new Payment();
		payment.setEpayId(epay.getId());
		payment.setModule(Payment.rank_module);
		payment.setOrderId(trank.getId());
		payment.setPayBody(trank.getName());
		payment.setAmount(trank.getPrice());
		payment.setPayFlag(Rank.NO);
		payment.userOptions(user);
		return this.init(payment, user);
	}
}