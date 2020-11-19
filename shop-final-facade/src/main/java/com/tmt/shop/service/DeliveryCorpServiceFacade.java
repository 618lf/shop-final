package com.tmt.shop.service;

import java.util.List;

import com.tmt.core.service.BaseServiceFacade;
import com.tmt.shop.entity.DeliveryCorp;

/**
 * 物流公司 管理
 * @author 超级管理员
 * @date 2016-01-20
 */
public interface DeliveryCorpServiceFacade extends BaseServiceFacade<DeliveryCorp,Long> {
	
	/**
	 * 保存
	 */
	public void save(DeliveryCorp deliveryCorp);
	
	/**
	 * 排序
	 * @param deliveryCorps
	 */
	public void updateSort(List<DeliveryCorp> deliveryCorps);
	
	/**
	 * 删除
	 */
	public void delete(List<DeliveryCorp> deliveryCorps);
	
	/**
	 * 查询所有的物流公司
	 * @return
	 */
	public List<DeliveryCorp> queryDeliveryCorps();
}