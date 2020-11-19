package com.tmt.shop.service;

import java.util.List;

import com.tmt.core.service.BaseServiceFacade;
import com.tmt.shop.entity.DeliveryCenter;

/**
 * 送货中心 管理
 * @author 超级管理员
 * @date 2016-01-20
 */
public interface DeliveryCenterServiceFacade extends BaseServiceFacade<DeliveryCenter,Long> {
	
	/**
	 * 保存
	 */
	public void save(DeliveryCenter deliveryCenter);
	
	
	/**
	 * 删除
	 */
	public void delete(List<DeliveryCenter> deliveryCenters);
	
	/**
	 * 获得所有的送货中心
	 * @return
	 */
	public List<DeliveryCenter> queryDeliveryCenters();
	
}
