package com.tmt.shop.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.service.BaseService;
import com.tmt.core.utils.Maps;
import com.tmt.shop.dao.DeliveryNotesDao;
import com.tmt.shop.entity.DeliveryNotes;

/**
 * 物流信息 管理
 * @author 超级管理员
 * @date 2016-03-12
 */
@Service("shopDeliveryNotesService")
public class DeliveryNotesService extends BaseService<DeliveryNotes,Long> implements DeliveryNotesServiceFacade{
	
	@Autowired
	private DeliveryNotesDao deliveryNotesDao;
	
	@Override
	protected BaseDaoImpl<DeliveryNotes, Long> getBaseDao() {
		return deliveryNotesDao;
	}
	
	/**
	 * 保存
	 */
	@Transactional
	public void save(DeliveryNotes deliveryNotes) {
		if( IdGen.isInvalidId(deliveryNotes.getId()) ) {
			this.insert(deliveryNotes);
		} else {
			this.update(deliveryNotes);
		}
	}
	
	/**
	 * 删除
	 */
	@Transactional
	public void delete(List<DeliveryNotes> deliveryNotess) {
		this.batchDelete(deliveryNotess);
	}
	
	/**
	 * 查询快递信息
	 * @param corpCode
	 * @param trackingNo
	 * @return
	 */
	public List<DeliveryNotes> findByCorpTrackingNo(String corpCode, String trackingNo) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("corpCode", corpCode);
		params.put("trackingNo", trackingNo);
		return this.queryForList("findByCorpTrackingNo", params);
	}
}