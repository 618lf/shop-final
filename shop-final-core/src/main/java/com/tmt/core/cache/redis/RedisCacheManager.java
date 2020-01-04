package com.tmt.core.cache.redis;

import java.util.Collection;

import com.tmt.core.cache.AbstractCacheManager;
import com.tmt.core.cache.Cache;
/**
 * 
 * 不需要事物支持的缓存管理
 * @author root
 *
 */
public class RedisCacheManager extends AbstractCacheManager {

	private Collection<Cache> caches;
	
	/**
	 * 配置cache的入口
	 * @param caches
	 */
	public void setCaches(Collection<Cache> caches) {
		this.caches = caches;
	}
	
	@Override
	protected Collection<? extends Cache> loadCaches() {
		return caches;
	}
	
	/**
	 * 获取cache
	 */
	@Override
	public Cache getCache(String name) {
		String _name = name;
		if(_name != null) {_name = _name.toUpperCase();}
		Cache cache = super.getCache(_name);
		if (cache == null) {//抛出异常
			cache = new RedisCache(_name);
		}
		return cache;
	}
}
