package com.tmt.shop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.service.BaseService;
import com.tmt.shop.dao.DeliveryCorpDao;
import com.tmt.shop.entity.DeliveryCorp;

/**
 * 物流公司 管理
 * @author 超级管理员
 * @date 2016-01-20
 */
@Service("shopDeliveryCorpService")
public class DeliveryCorpService extends BaseService<DeliveryCorp,Long> implements DeliveryCorpServiceFacade{
	
	@Autowired
	private DeliveryCorpDao deliveryCorpDao;
	
	@Override
	protected BaseDaoImpl<DeliveryCorp, Long> getBaseDao() {
		return deliveryCorpDao;
	}
	
	/**
	 * 保存
	 */
	@Transactional
	public void save(DeliveryCorp deliveryCorp) {
		if( IdGen.isInvalidId(deliveryCorp.getId()) ) {
			this.insert(deliveryCorp);
		} else {
			this.update(deliveryCorp);
		}
	}
	
	
    @Transactional
	public void updateSort(List<DeliveryCorp> deliveryCorps ) {
		this.batchUpdate("updateSort", deliveryCorps);
	}
	/**
	 * 删除
	 */
	@Transactional
	public void delete(List<DeliveryCorp> deliveryCorps) {
		this.batchDelete(deliveryCorps);
	}
	
	/**
	 * 查询所有的物流公司
	 * @return
	 */
	public List<DeliveryCorp> queryDeliveryCorps() {
		return this.getAll();
	}
}