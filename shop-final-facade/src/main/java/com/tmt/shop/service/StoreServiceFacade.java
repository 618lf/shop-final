package com.tmt.shop.service;

import com.tmt.core.service.BaseServiceFacade;
import com.tmt.shop.entity.Store;

/**
 * 店铺管理 管理
 * @author 超级管理员
 * @date 2017-01-10
 */
public interface StoreServiceFacade extends BaseServiceFacade<Store,Long> {
	
	/**
	 * 保存
	 */
	public void save(Store store);
}
