package com.tmt.shop.service;

import java.math.BigDecimal;
import java.util.List;

import com.tmt.core.service.BaseServiceFacade;
import com.tmt.shop.entity.Epayment;
import com.tmt.system.entity.User;

/**
 * 企业支付 管理
 * @author 超级管理员
 * @date 2015-12-21
 */
public interface EpaymentServiceFacade extends BaseServiceFacade<Epayment,Long> {
	
	/**
	 * 保存
	 */
	public void save(Epayment epayment);
	
	/**
	 * 删除
	 */
	public void delete(List<Epayment> epayments);
	
	/**
	 * 付款
	 * @param sn
	 * @param remarks
	 * @param amount
	 * @param user
	 * @param epayId
	 * @param openId
	 */
	public void pay(String sn, String remarks, BigDecimal amount, User user, Long epayId, String openId);
}
