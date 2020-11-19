package com.tmt.cms.service;

import java.util.List;

import com.tmt.cms.entity.AdPosition;
import com.tmt.core.service.BaseServiceFacade;

/**
 * 广告管理 管理
 * @author 超级管理员
 * @date 2016-07-20
 */
public interface AdPositionServiceFacade extends BaseServiceFacade<AdPosition,Long> {
	
	/**
	 * 保存
	 */
	public void save(AdPosition adPosition);
	
	/**
	 * 删除
	 */
	public void delete(List<AdPosition> adPositions);
	
	/**
	 * 
	 * @return
	 */
	public AdPosition getWithAds(Long id);
}