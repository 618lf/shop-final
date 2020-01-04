package com.tmt.core.security.principal.support;

import org.springframework.beans.factory.FactoryBean;

import com.tmt.core.cache.CacheManager;
import com.tmt.core.cache.ehcache.EhCacheCacheManager;
import com.tmt.core.security.principal.Session;
import com.tmt.core.security.principal.SessionRepository;

/**
 * 用于创建 SessionRespository
 * 
 * @author lifeng
 */
public class SessionRespositoryFactoryBean implements FactoryBean<SessionRepository<? extends Session>> {

	private CacheManager cacheManager;
	private int sessionTimeout = 1800;

	public int getSessionTimeout() {
		return sessionTimeout;
	}

	public void setSessionTimeout(int sessionTimeout) {
		this.sessionTimeout = sessionTimeout;
	}

	public CacheManager getCacheManager() {
		return cacheManager;
	}

	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	/**
	 * 根据配置的缓存类型来判断需要使用的 session
	 */
	@Override
	public SessionRepository<? extends Session> getObject() throws Exception {
		SessionRepository<? extends Session> sessionRepository = null;
		if (cacheManager instanceof EhCacheCacheManager) {
			sessionRepository = new EhCacheSessionRepository(cacheManager);
		} else {
			sessionRepository = new RedisSessionRepository();
		}
		sessionRepository.setSessionTimeout(sessionTimeout);
		return sessionRepository;
	}

	@Override
	public Class<?> getObjectType() {
		return SessionRepository.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}
