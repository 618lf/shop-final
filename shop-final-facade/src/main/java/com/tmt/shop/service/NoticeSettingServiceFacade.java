package com.tmt.shop.service;

import com.tmt.core.service.BaseServiceFacade;
import com.tmt.shop.entity.NoticeSetting;

public interface NoticeSettingServiceFacade extends BaseServiceFacade<NoticeSetting, Byte> {

	/**
	 * 修改配置
	 * 
	 * @param setting
	 */
	public void updateTmsg(NoticeSetting setting);

	/**
	 * 修改配置
	 * 
	 * @param setting
	 */
	public void updateSitemsg(NoticeSetting setting);

	/**
	 * 修改配置
	 * 
	 * @param setting
	 */
	public void updateSmsg(NoticeSetting setting);

	/**
	 * 得到相关类型的设置
	 * 
	 * @param type
	 * @return
	 */
	public NoticeSetting getByType(Byte type);
	
	/**
	 * 修改配置
	 * 
	 * @param setting
	 */
	public void updateTemplate(NoticeSetting setting);
}