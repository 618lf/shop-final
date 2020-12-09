package com.tmt.shop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.service.BaseService;
import com.tmt.shop.dao.DeliveryTemplateDao;
import com.tmt.shop.entity.DeliveryTemplate;

/**
 * 快递单模板 管理
 * @author 超级管理员
 * @date 2016-02-23
 */
@Service("shopDeliveryTemplateService")
public class DeliveryTemplateService extends BaseService<DeliveryTemplate,Long> implements DeliveryTemplateServiceFacade{
	
	@Autowired
	private DeliveryTemplateDao deliveryTemplateDao;
	
	@Override
	protected BaseDaoImpl<DeliveryTemplate, Long> getBaseDao() {
		return deliveryTemplateDao;
	}
	
	/**
	 * 保存
	 */
	@Transactional
	public void save(DeliveryTemplate deliveryTemplate) {
		if(IdGen.isInvalidId(deliveryTemplate.getId())) {
			this.insert(deliveryTemplate);
		} else {
			this.update(deliveryTemplate);
		}
	}
	
	
	/**
	 * 删除
	 */
	@Transactional
	public void delete(List<DeliveryTemplate> deliveryTemplates) {
		this.batchDelete(deliveryTemplates);
	}
}
