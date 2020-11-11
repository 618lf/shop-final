package com.tmt.wechat.service;

import com.tmt.core.service.BaseServiceFacade;
import com.tmt.wechat.entity.MetaSetting;

/**
 * 回复配置 管理
 * @author 超级管理员
 * @date 2016-09-27
 */
public interface MetaSettingServiceFacade extends BaseServiceFacade<MetaSetting,Long> {
	
	/**
	 * 保存
	 */
	public void save(MetaSetting metaSetting);
	
	/**
	 * 根据APP_ID 获取配置
	 * @param appId
	 * @return
	 */
	public MetaSetting getByAppId(String appId);
}