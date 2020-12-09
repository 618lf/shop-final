package com.tmt.shop.utils;

import com.tmt.core.utils.CacheUtils;
import com.tmt.core.utils.SpringContextHolder;
import com.tmt.core.utils.time.DateUtils;
import com.tmt.shop.entity.ShopConstant;
import com.tmt.shop.entity.Store;
import com.tmt.shop.service.StoreServiceFacade;

public class StoreUtils {

    /**
     * 得到默认的店铺	
     * @return
     */
	public static Store getDefaultStore(){
		String key = new StringBuilder(ShopConstant.STORE_CACHE).append(Store.DEFAULT_STORE).toString();
		Store store = CacheUtils.get(key);
		if (store == null) {
			StoreServiceFacade storeService = SpringContextHolder.getBean(StoreServiceFacade.class);
			store = storeService.get(Store.DEFAULT_STORE);
			store.setUpdateTime(DateUtils.getTimeStampNow().getTime());
			CacheUtils.put(key, store);
		}
		return store;
	}
	
	/**
	 * 删除缓存
	 */
	public static void clearCache() {
		String key = new StringBuilder(ShopConstant.STORE_CACHE).append(Store.DEFAULT_STORE).toString();
		CacheUtils.evict(key);
	}
	
	/**
	 * 更新店铺
	 */
	public static void updateStore() {
		String key = new StringBuilder(ShopConstant.STORE_CACHE).append(Store.DEFAULT_STORE).toString();
		Store store = CacheUtils.get(key);
		if (store != null) {
			store.setUpdateTime(DateUtils.getTimeStampNow().getTime());
			CacheUtils.put(key, store);
		}
	}
}