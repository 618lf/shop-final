package com.tmt.shop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.core.persistence.BaseDao;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.service.BaseService;
import com.tmt.shop.dao.DeliverySchemeDao;
import com.tmt.shop.entity.DeliveryScheme;

/**
 * 配送方案 管理
 * @author 超级管理员
 * @date 2016-11-06
 */
@Service("shopDeliverySchemeService")
public class DeliverySchemeService extends BaseService<DeliveryScheme,Long> implements DeliverySchemeServiceFacade{
	
	@Autowired
	private DeliverySchemeDao deliverySchemeDao;
	
	@Override
	protected BaseDao<DeliveryScheme, Long> getBaseDao() {
		return deliverySchemeDao;
	}
	
	/**
	 * 保存
	 */
	@Transactional
	public void save(DeliveryScheme deliveryScheme) {
		if(IdGen.isInvalidId(deliveryScheme.getId())) {
			this.insert(deliveryScheme);
		} else {
			this.update(deliveryScheme);
		}
		
		// 设置为非默认
		this.setDefault(deliveryScheme);
	}
	
	// 设置默认
	public void setDefault(DeliveryScheme deliveryScheme) {
		// 将其他设置为非默认
		if (deliveryScheme.getIsDefault() == 1) {
			this.update("updateNoDefault", deliveryScheme);
		}
	}
	
	/**
	 * 删除
	 */
	@Transactional
	public void delete(List<DeliveryScheme> deliverySchemes) {
		this.batchDelete(deliverySchemes);
	}
	
}
