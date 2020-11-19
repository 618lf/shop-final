package com.tmt.shop.service;

import java.util.List;

import com.tmt.core.service.BaseServiceFacade;
import com.tmt.shop.entity.Order;
import com.tmt.shop.entity.Refunds;
import com.tmt.system.entity.User;

/**
 * 退款管理 管理
 * @author 超级管理员
 * @date 2016-01-21
 */
public interface RefundsServiceFacade extends BaseServiceFacade<Refunds,Long> {
	
	/**
	 * 准备发起付款
	 * @param refunds
	 * @return
	 */
	public Refunds prepareRefunds(Order order);
	
	/**
	 * 保存
	 */
	public void save(Refunds refunds, User operater);
	
	/**
	 * 准备发起付款
	 * @param refunds
	 * @return
	 */
	public Boolean refundsProcess(Order order);
	
	/**
	 * 删除
	 */
	public void delete(List<Refunds> refundss);
	
	/**
	 * 订单的退款信息
	 * @param orderId
	 * @return
	 */
	public List<Refunds> queryRefundsByOrderId(Long orderId);
}
