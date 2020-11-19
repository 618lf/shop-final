package com.tmt.shop.service;

import java.util.List;

import com.tmt.core.service.BaseServiceFacade;
import com.tmt.shop.entity.Order;
import com.tmt.shop.entity.Shipping;
import com.tmt.system.entity.User;

/**
 * 发货管理 管理
 * @author 超级管理员
 * @date 2016-01-21
 */
public interface ShippingServiceFacade extends BaseServiceFacade<Shipping,Long> {
	
	/**
	 * 保存
	 */
	public void save(Shipping shipping);
	
	/**
	 * 保存
	 */
	public void shipping(Order order, User user, Shipping shipping);
	
	/**
	 * 删除
	 */
	public void delete(List<Shipping> shippings);
	
	/**
	 * 并查询明细
	 * @param id
	 * @return
	 */
	public Shipping getWithItems(Long id);
	
	/**
	 * 查询订单的发货信息
	 * @param orderId
	 * @return
	 */
	public List<Shipping> queryShippingsByOrderId(Long orderId);
}