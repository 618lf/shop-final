package com.tmt.shop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.core.service.BaseService;
import com.tmt.shop.dao.ProductNotifyDao;
import com.tmt.shop.entity.ProductNotify;

/**
 * 到货通知 管理
 * @author 超级管理员
 * @date 2016-01-21
 */
@Service("shopProductNotifyService")
public class ProductNotifyService extends BaseService<ProductNotify,Long> implements ProductNotifyServiceFacade{
	
	@Autowired
	private ProductNotifyDao productNotifyDao;
	
	@Override
	protected BaseDaoImpl<ProductNotify, Long> getBaseDao() {
		return productNotifyDao;
	}
	
	/**
	 * 保存
	 */
	@Transactional
	public void save(ProductNotify productNotify) {
		this.insert(productNotify);
	}
	
	/**
	 * 删除
	 */
	@Transactional
	public void delete(List<ProductNotify> productNotifys) {
		this.batchDelete(productNotifys);
	}
}