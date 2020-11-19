package com.tmt.shop.service;

import java.util.List;

import com.tmt.core.service.BaseServiceFacade;
import com.tmt.shop.entity.DeliveryScheme;

/**
 * 配送方案 管理
 * @author 超级管理员
 * @date 2016-11-06
 */
public interface DeliverySchemeServiceFacade extends BaseServiceFacade<DeliveryScheme,Long> {
	
	/**
	 * 保存
	 */
	public void save(DeliveryScheme deliveryScheme);
	
	/**
	 * 删除
	 */
	public void delete(List<DeliveryScheme> deliverySchemes);
}