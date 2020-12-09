package com.tmt.shop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.service.BaseService;
import com.tmt.shop.dao.DeliveryCenterDao;
import com.tmt.shop.entity.DeliveryCenter;
import com.tmt.shop.utils.DeliveryCenterUtils;

/**
 * 送货中心 管理
 * @author 超级管理员
 * @date 2016-01-20
 */
@Service("shopDeliveryCenterService")
public class DeliveryCenterService extends BaseService<DeliveryCenter,Long> implements DeliveryCenterServiceFacade{
	
	@Autowired
	private DeliveryCenterDao deliveryCenterDao;
	
	@Override
	protected BaseDaoImpl<DeliveryCenter, Long> getBaseDao() {
		return deliveryCenterDao;
	}
	
	/**
	 * 保存
	 */
	@Transactional
	public void save(DeliveryCenter deliveryCenter) {
		if( IdGen.isInvalidId(deliveryCenter.getId()) ) {
			this.insert(deliveryCenter);
		} else {
			this.update(deliveryCenter);
		}
		
		DeliveryCenterUtils.clearCache();
	}
	
	
	/**
	 * 删除
	 */
	@Transactional
	public void delete(List<DeliveryCenter> deliveryCenters) {
		this.batchDelete(deliveryCenters);
		DeliveryCenterUtils.clearCache();
	}

	/**
	 * 所有的送货中心
	 */
	@Override
	public List<DeliveryCenter> queryDeliveryCenters() {
		return this.getAll();
	}
}
