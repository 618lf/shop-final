package com.tmt.cms.service;

import java.util.List;

import com.tmt.cms.entity.Navigation;
import com.tmt.core.service.BaseServiceFacade;

/**
 * 导航管理 管理
 * @author 超级管理员
 * @date 2016-07-20
 */
public interface NavigationServiceFacade extends BaseServiceFacade<Navigation,Long> {
	
	/**
	 * 保存
	 */
	public void save(Navigation navigation);
	
	/**
	 * 删除
	 */
	public void delete(List<Navigation> navigations);
}
