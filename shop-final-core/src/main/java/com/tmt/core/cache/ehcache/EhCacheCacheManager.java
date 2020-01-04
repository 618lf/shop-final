package com.tmt.core.cache.ehcache;

import java.util.Collection;
import java.util.LinkedHashSet;

import com.tmt.core.cache.AbstractCacheManager;
import com.tmt.core.cache.Cache;

import net.sf.ehcache.Ehcache;

/**
 * 复写 EhCacheCacheManager
 * @author lifeng
 *
 */
public class EhCacheCacheManager extends AbstractCacheManager{

	private net.sf.ehcache.CacheManager cacheManager;

	public EhCacheCacheManager() {}

	public EhCacheCacheManager(net.sf.ehcache.CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	public void setCacheManager(net.sf.ehcache.CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	public net.sf.ehcache.CacheManager getCacheManager() {
		return this.cacheManager;
	}


	@Override
	protected Collection<Cache> loadCaches() {
		String[] names = this.cacheManager.getCacheNames();
		Collection<Cache> caches = new LinkedHashSet<Cache>(names.length);
		for (String name : names) {
			caches.add(new EhCacheCache(this.cacheManager.getEhcache(name)));
		}
		return caches;
	}

	@Override
	public Cache getCache(String name) {
		Cache cache = super.getCache(name);
		if (cache == null) {
			Ehcache ehcache = this.cacheManager.getEhcache(name);
			if (ehcache != null) {
				cache = new EhCacheCache(ehcache);
				addCache(cache);
			}
		}
		return cache;
	}
}