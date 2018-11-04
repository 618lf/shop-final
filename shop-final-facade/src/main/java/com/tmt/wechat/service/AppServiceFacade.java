package com.tmt.wechat.service;

import java.util.List;

import com.tmt.common.service.BaseServiceFacade;
import com.tmt.wechat.entity.App;

/**
 * 公众号管理
 * @author root
 */
public interface AppServiceFacade extends BaseServiceFacade<App, String>{

	/**
	 * 保存
	 * @param app
	 */
	public void save(App app);
	
	/**
	 * 删除
	 * @param apps
	 */
	public void delete(List<App> apps);
	
	/**
	 * 根据域名获取
	 * @return
	 */
	public App getDomain(String domain);
	
	/**
	 * 根据微信号获取
	 * @return
	 */
	public App getEvent(String wxnum);
	
}