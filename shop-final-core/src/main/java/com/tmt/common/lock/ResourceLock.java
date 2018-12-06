package com.tmt.common.lock;

import com.tmt.common.cache.CacheManager;
import com.tmt.common.cache.redis.RedisCacheManager;
import com.tmt.common.lock.impl.StrictRedisLock;
import com.tmt.common.lock.impl.TransactionalLock;
import com.tmt.common.utils.SpringContextHolder;


/**
 * 资源锁
 * @author lifeng
 *
 */
public class ResourceLock {

	private static Boolean USE_REDIS = null;
	
	/**
	 * 得到锁 -- 优先使用 redis 获得锁
	 * @param name
	 * @return
	 */
	public static Lock getLock(String name) {
		
		// 是否使用redis
		if (USE_REDIS == null) {
			CacheManager cacheManager  = SpringContextHolder.getBean(CacheManager.class);
			if (cacheManager != null && (cacheManager instanceof RedisCacheManager)) {
				USE_REDIS = true;
			} else {
				USE_REDIS = false;
			}
		}
		
		if (USE_REDIS) {
			return new StrictRedisLock(name);
		}
		
		// 默认使用基于数据的单需要改进,数据库事务有超时时间
		return new TransactionalLock(name);
	}
}