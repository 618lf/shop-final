package com.tmt.wechat.service;

import java.util.List;

import com.tmt.common.service.BaseServiceFacade;
import com.tmt.wechat.entity.Menu;

/**
 * 自定义菜单 管理
 * @author 超级管理员
 * @date 2016-09-13
 */
public interface MenuServiceFacade extends BaseServiceFacade<Menu,Long> {
	
	/**
	 * 保存
	 */
	public void save(Menu template, List<Menu> menus);
	
	/**
	 * 删除
	 */
	public void delete(List<Menu> menus);
	
	/**
	 * 查询公众号的菜单
	 * @param appId
	 * @return
	 */
	public List<Menu> queryMenusByAppId(String appId);
}