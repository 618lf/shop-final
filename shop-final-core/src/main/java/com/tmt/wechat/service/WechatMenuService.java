package com.tmt.wechat.service;

import com.tmt.wechat.bean.menu.MenuButtons;
import com.tmt.wechat.exception.WechatErrorException;

public interface WechatMenuService {

	/**
	 * 创建菜单
	 * 
	 * @param menu
	 * @return
	 * @throws WechatErrorException
	 */
	boolean menuCreate(MenuButtons menuItem) throws WechatErrorException;
}