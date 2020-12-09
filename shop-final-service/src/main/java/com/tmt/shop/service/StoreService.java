package com.tmt.shop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.core.persistence.BaseDao;
import com.tmt.core.service.BaseService;
import com.tmt.shop.dao.StoreDao;
import com.tmt.shop.entity.Store;
import com.tmt.shop.utils.StoreUtils;

/**
 * 店铺管理 管理
 * @author 超级管理员
 * @date 2017-01-10
 */
@Service("shopStoreService")
public class StoreService extends BaseService<Store,Long> implements StoreServiceFacade{
	
	@Autowired
	private StoreDao storeDao;
	
	@Override
	protected BaseDao<Store, Long> getBaseDao() {
		return storeDao;
	}
	
	/**
	 * 保存
	 */
	@Transactional
	public void save(Store store) {
		store.setId(Store.DEFAULT_STORE);
		this.update(store);
		StoreUtils.clearCache();
	}
}