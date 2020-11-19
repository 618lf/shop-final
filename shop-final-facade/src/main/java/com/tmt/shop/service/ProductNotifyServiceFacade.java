package com.tmt.shop.service;

import java.util.List;

import com.tmt.core.service.BaseServiceFacade;
import com.tmt.shop.entity.ProductNotify;

/**
 * 到货通知 管理
 * @author 超级管理员
 * @date 2016-01-21
 */
public interface ProductNotifyServiceFacade extends BaseServiceFacade<ProductNotify,Long> {
	
	/**
	 * 保存
	 */
	public void save(ProductNotify productNotify);
	
	/**
	 * 删除
	 */
	public void delete(List<ProductNotify> productNotifys);
	
}
