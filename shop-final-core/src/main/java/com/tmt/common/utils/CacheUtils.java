package com.tmt.common.utils;

import java.util.List;

import com.tmt.common.cache.Cache;
import com.tmt.common.cache.CacheManager;
import com.tmt.common.exception.BaseRuntimeException;

/**
 * Cache工具类 : 基于 BaseCache 提供服务
 * @author ThinkGem
 * @version 2013-5-29
 */
public class CacheUtils{
	
	//缓存对象（这里只是一个引用）
	public static Cache sysCache;
	public static Cache dictCache;
	public static Cache sessCache;
	
	public static Cache getSysCache() {
		if (sysCache == null) {
			sysCache = CacheUtils.getCache("sys");
		}
		return sysCache;
	}

	public static Cache getDictCache() {
		if (dictCache == null) {
			dictCache = CacheUtils.getCache("dict");
		}
		return dictCache;
	}

	public static Cache getSessCache() {
		if (sessCache == null) {
			sessCache = CacheUtils.getCache("sess");
		}
		return sessCache;
	}

	//***************默认提供对系统缓存的操作***********************
	
	/**
	 * 得到缓存的名称
	 * @return
	 */
	public static String getName() {
		return CacheUtils.getSysCache().getName();
	}

	/**
	 * 得到缓存的操作对象
	 * @return
	 */
	public static Object getNativeCache() {
		return CacheUtils.getSysCache().getNativeCache();
	}

	/**
	 * 得到值
	 * @param key
	 * @return
	 */
	public static <T> T get(Object key) {
		return CacheUtils.getSysCache().get(key);
	}

	/**
	 * 使用默认的生命周期
	 * @param key
	 * @param value
	 */
	public static void put(Object key, Object value) {
		CacheUtils.getSysCache().put(key, value);
	}
	
	/**
	 * 固定生命周期的 缓存(存活10分钟)
	 * @param key
	 * @param value
	 * @param timeToLive(秒)
	 */
	public static void putWithLiveTime(Object key, Object value, int timeToLive) {
		CacheUtils.getSysCache().put(key, value, timeToLive);
	}
	
	/**
	 * 删除值
	 * @param key
	 */
	public static void evict(Object key) {
		CacheUtils.getSysCache().delete(key);
	}

	/**
	 * 得到所有值
	 * @param pattern
	 * @return
	 */
	public static <T> List<T> getKeys(String pattern) {
		return CacheUtils.getSysCache().values(pattern);
	}
	
	/**
	 * 删除key匹配pattern的数据
	 * @param pattern
	 */
	public static void deletePattern(String pattern) {
		CacheUtils.getSysCache().deletePattern(pattern);
	}
	
	/**
	 * 获得一个缓存
	 * @param cacheName
	 * @return
	 */
	public static Cache getCache(String cacheName){
		CacheManager cacheManager = SpringContextHolder.getBean("cacheManager");
		Cache cache = (Cache)cacheManager.getCache(cacheName);
		if (cache == null){
			throw new BaseRuntimeException("没有相应的缓存,请配置:" + cacheName);
		}
		return cache;
	}
}