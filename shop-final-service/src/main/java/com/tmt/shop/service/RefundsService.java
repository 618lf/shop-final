package com.tmt.shop.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.core.entity.AjaxResult;
import com.tmt.core.entity.BaseEntity;
import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.core.service.BaseService;
import com.tmt.core.utils.BigDecimalUtil;
import com.tmt.core.utils.Lists;
import com.tmt.core.utils.Sets;
import com.tmt.core.utils.StringUtils;
import com.tmt.shop.dao.RefundsDao;
import com.tmt.shop.entity.Order;
import com.tmt.shop.entity.Payment;
import com.tmt.shop.entity.Refunds;
import com.tmt.shop.utils.EpayUtils;
import com.tmt.shop.utils.SnUtils;
import com.tmt.system.entity.User;

/**
 * 应该调用 取消订单，而不是走企业付款
 * 退款管理 管理
 * @author 超级管理员
 * @date 2016-01-21
 */
@Service("shopRefundsService")
public class RefundsService extends BaseService<Refunds,Long> implements RefundsServiceFacade{
	
	@Autowired
	private RefundsDao refundsDao;
	@Autowired
	private OrderService orderService;
	@Autowired
	private PaymentServiceFacade paymentService;
	
	@Override
	protected BaseDaoImpl<Refunds, Long> getBaseDao() {
		return refundsDao;
	}
	
	/**
	 * 准备发起退款 -- 不能超过实际的支付金额
	 * @param order
	 * @return
	 */
	public Refunds prepareRefunds(Order order) {
		Refunds refunds = new Refunds(); refunds.setType((byte)1);
		Order _order = orderService.get(order.getId());
		if (_order != null) { 
			List<Payment> payments = paymentService.queryPaymentsByOrderId(_order.getId());
			for(Payment payment: payments) {
				if (payment.getPayFlag() == BaseEntity.YES) {
					refunds.setAmount(BigDecimalUtil.add(refunds.getAmount(), payment.getAmount()));
				}
				if (StringUtils.isNotBlank(payment.getTransactionId())
						&& payment.getEpayId() != null && payment.getPayFlag() == BaseEntity.YES) {
					refunds.setType((byte)0);
				}
			}
			// 线下收的款
			if (refunds.getType() == 1) {
				refunds.setAmount(order.getAmountPaid());
			}
			refunds.setOrderId(_order.getId());
			refunds.setOrderSn(_order.getSn());
		}
		return refunds;
	}
	
	/**
	 * 保存 -- 直接调用微信的退款操作
	 */
	@Transactional
	public void save(Refunds refunds, User operater) {
		Order order = this.orderService.get(refunds.getOrderId());
		if (order != null && order.isRefundable()) {
		    this.orderService.lock(order);
		    refunds.setSn(SnUtils.createRefundsSn());
		    
		    // 走网上退款
		    if (refunds.getType() == 0) {
		    	int i = 1;
		    	List<Refunds> refundss = Lists.newArrayList();
		    	List<Payment> payments = paymentService.queryPaymentsByOrderId(order.getId());
		    	for(Payment payment: payments) {
		    		if (payment.getPayFlag() != BaseEntity.YES) {
		    			continue;
		    		}
		    		Refunds newRefunds = refunds.copy();
		    		newRefunds.userOptions(operater);
		    		newRefunds.setSn(new StringBuilder(refunds.getSn()).append("-").append(i++).toString());
		    		newRefunds.setAccount(payment.getAccount());
		    		newRefunds.setAmount(payment.getAmount());
		    		newRefunds.setPayee(payment.getPayer());
		    		if (payment.getEpayId() != null && StringUtils.isNotBlank(payment.getTransactionId())) {
		    			newRefunds.setEpayId(payment.getEpayId());
		    			newRefunds.setTransactionId(payment.getTransactionId());
		    			newRefunds.setState(BaseEntity.DEL_FLAG_NORMAL); // 未提交申请，定时任务会自动提交
		    			newRefunds.setType((byte)0);
		    		} else {
		    			newRefunds.setAccount(refunds.getAccount());
		    			newRefunds.setPayee(refunds.getPayee());
		    			newRefunds.setState(BaseEntity.DEL_FLAG_AUDIT); // 处理完成
		    			newRefunds.setType((byte)1);
		    		}
		    		refundss.add(newRefunds);
		    	}
		    	// 保存退款申请,订单进入退款处理状态
		    	this.batchInsert(refundss);
		    	this.orderService.refundsProcess(order, operater, refunds);
		    }
		    
		    // 全部走线下付款（或者其他的退款方式）
		    else {
		    	refunds.setState(BaseEntity.DEL_FLAG_AUDIT); // 处理完成
				this.insert(refunds);
				this.orderService.refunds(order, operater, refunds);
		    }
		}
	}
	
	/**
	 * 处理退货请求
	 */
	@Override
	@Transactional
	public Boolean refundsProcess(Order order) {
		this.orderService.lock(order);
		if (order.isRefundProcessable()) {
			Boolean allProcessed = Boolean.TRUE; Refunds all = new Refunds(); Set<String> accounts = Sets.newHashSet();
			List<Refunds> refunds = this.queryRefundsByOrderId(order.getId());
			for(Refunds refund: refunds) {
				if (refund.getState() == 2) {
					accounts.add(refund.getAccount());
					all.setAmount(BigDecimalUtil.add(all.getAmount(), refund.getAmount()));
					all.setAccount(StringUtils.join(accounts, ","));
				}
				if (refund.getType() != 0 || refund.getState() == 2) {
					continue;
				}
				if (refund.getState() == 0) {
					AjaxResult result = EpayUtils.refundsPayment(refund);
					if (result.getSuccess()) {
						refund.setState(Refunds.DEL_FLAG_DELETE);
					} else {
						refund.setState(Refunds.DEL_FLAG_NORMAL);
						refund.setRemarks(result.getMsg());
					}
				} else if(refund.getState() == 1) {
					AjaxResult result = EpayUtils.refundsQuery(refund);
					if (result.getSuccess()) {
						refund.setState(Refunds.DEL_FLAG_AUDIT);
					} else {
						refund.setState(Refunds.DEL_FLAG_DELETE);
						refund.setRemarks(result.getMsg());
					}
				}
				
				// 这次课程处理成功了
				if (refund.getState() != Refunds.DEL_FLAG_AUDIT) {
					allProcessed = Boolean.FALSE;
				}  else {
					all.setAmount(BigDecimalUtil.add(all.getAmount(), refund.getAmount()));
				}
				
				// 批量更新
				this.batchUpdate("updateState", refunds);
			}
			if (allProcessed) {
				this.orderService.refunds(order, null, all);
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}

	/**
	 * 删除
	 */
	@Transactional
	public void delete(List<Refunds> refundss) {
		this.batchDelete(refundss);
	}
	
	/**
	 * 订单的退款信息
	 * @param orderId
	 * @return
	 */
	public List<Refunds> queryRefundsByOrderId(Long orderId) {
		return this.queryForList("queryRefundsByOrderId", orderId);
	}
}