package com.tmt.shop.service;

import java.util.List;

import com.tmt.core.service.BaseServiceFacade;
import com.tmt.shop.entity.DeliveryTemplate;

/**
 * 快递单模板 管理
 * @author 超级管理员
 * @date 2016-02-23
 */
public interface DeliveryTemplateServiceFacade extends BaseServiceFacade<DeliveryTemplate,Long> {
	
	/**
	 * 保存
	 */
	public void save(DeliveryTemplate deliveryTemplate);
	
	
	/**
	 * 删除
	 */
	public void delete(List<DeliveryTemplate> deliveryTemplates);
}
