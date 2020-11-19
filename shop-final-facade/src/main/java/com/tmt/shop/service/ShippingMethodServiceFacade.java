package com.tmt.shop.service;

import java.util.List;

import com.tmt.core.service.BaseServiceFacade;
import com.tmt.shop.entity.PaymentMethod;
import com.tmt.shop.entity.PaymentShiopingMethod;
import com.tmt.shop.entity.ShippingMethod;

/**
 * 配送方式 管理
 * @author 超级管理员
 * @date 2016-01-20
 */
public interface ShippingMethodServiceFacade extends BaseServiceFacade<ShippingMethod,Long> {
	
	/**
	 * 带出区域配置
	 * @param id
	 * @return
	 */
	public ShippingMethod getWithAreas(Long id);
	
	/**
	 * 查询所支持的支付方式
	 * @param id
	 * @return
	 */
	public ShippingMethod getWithPaymentMethods(Long id);
	
	/**
	 * 保存
	 */
	public void save(ShippingMethod shippingMethod);
	
	/**
	 * 排序
	 * @param shippingMethods
	 */
	public void updateSort(List<ShippingMethod> shippingMethods);
    
	/**
	 * 删除
	 */
	public void delete(List<ShippingMethod> shippingMethods);
	
	/**
	 * 保存
	 */
	public void savePaymentMethods(ShippingMethod shippingMethod);
	
	/**
	 * 根据配送方式查询关联的支付方式
	 * @return
	 */
	public List<PaymentShiopingMethod> queryByShiopingMethodId(Long shiopingMethodId);
	
	/**
	 * 得到所有的配送方式(并查询出相应的支付方式)
	 * @return
	 */
	public List<ShippingMethod> queryShippingMethods();
	
	/**
	 * 获得指定的货运方式
	 * @param id
	 * @return
	 */
	public ShippingMethod getShippingMethod(Long id);
	
	/**
	 * 默认的货运方式
	 * @param paymentMethod
	 * @return
	 */
	public  ShippingMethod getDefaultShippingMethod(PaymentMethod paymentMethod);
}