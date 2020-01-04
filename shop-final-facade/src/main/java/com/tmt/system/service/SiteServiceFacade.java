package com.tmt.system.service;

import com.tmt.core.service.BaseServiceFacade;
import com.tmt.system.entity.Site;

/**
 * 站点设置 管理
 * @author 超级管理员
 * @date 2016-01-18
 */
public interface SiteServiceFacade extends BaseServiceFacade<Site, Long>{
	
	/**
	 * 基础保存
	 */
	public void baseSave(Site site);
	
	/**
	 * 安全保存
	 */
	public void safeSave(Site site);
	
	/**
	 * email配置保存
	 */
	public void emailSave(Site site);
	
	/**
	 * 得到站点配置
	 * @return
	 */
	public Site getSite();
}