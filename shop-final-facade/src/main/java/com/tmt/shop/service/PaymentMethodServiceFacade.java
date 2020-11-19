package com.tmt.shop.service;

import java.util.List;

import com.tmt.core.service.BaseServiceFacade;
import com.tmt.shop.entity.PaymentMethod;

/**
 * 支付方式 管理
 * @author 超级管理员
 * @date 2015-11-04
 */
public interface PaymentMethodServiceFacade extends BaseServiceFacade<PaymentMethod,Long> {
	
	/**
	 * 保存
	 */
	public void save(PaymentMethod paymentMethod);
	
	/**
	 * 排序
	 * @param paymentMethods
	 */
	public void updateSort(List<PaymentMethod> paymentMethods);
	
	/**
	 * 删除
	 */
	public void delete(List<PaymentMethod> paymentMethods);
	
	/**
	 * 获得指定的支付方式
	 * @param id
	 * @return
	 */
	public PaymentMethod getPaymentMethod(Long id);
	
	/**
	 * 获得默认的支付方式
	 * @return
	 */
	public PaymentMethod getDefaultPaymentMethod();
	
	/**
	 * 获得支付方式
	 * @return
	 */
	public List<PaymentMethod> queryPaymentMethods();
}
