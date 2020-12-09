package com.tmt.map.service;

import com.tmt.common.entity.AjaxResult;

/**
 * 地图服务
 * @author root
 *
 */
public interface MapService {

	/**
	 * 根据Ip 获取周边
	 * @param ip
	 * @return
	 */
	public AjaxResult getLocationPois(String ip);
	
	/**
	 * 查询位置
	 * @param query
	 * @return
	 */
	public AjaxResult queryLocationPois(String query);
}