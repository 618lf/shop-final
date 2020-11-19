package com.tmt.shop.service;

import java.util.List;

import com.tmt.core.service.BaseServiceFacade;
import com.tmt.shop.entity.DeliveryNotes;

/**
 * 物流信息 管理
 * @author 超级管理员
 * @date 2016-03-12
 */
public interface DeliveryNotesServiceFacade extends BaseServiceFacade<DeliveryNotes,Long> {
	
	/**
	 * 保存
	 */
	public void save(DeliveryNotes deliveryNotes);
	
	/**
	 * 删除
	 */
	public void delete(List<DeliveryNotes> deliveryNotess);
	
	/**
	 * 查询快递信息
	 * @param corpCode
	 * @param trackingNo
	 * @return
	 */
	public List<DeliveryNotes> findByCorpTrackingNo(String corpCode, String trackingNo);
}