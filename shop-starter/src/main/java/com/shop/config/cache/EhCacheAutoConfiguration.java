package com.shop.config.cache;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.tmt.common.cache.ehcache.EhCacheCacheManager;

/**
 * 缓存配置
 * 
 * @author lifeng
 */
@Configuration
@ConditionalOnProperty(prefix = "spring.cache", name = "enableEhCache", matchIfMissing = false)
public class EhCacheAutoConfiguration {

	@Bean
	public EhCacheManagerFactoryBean ehCacheManager() {
		EhCacheManagerFactoryBean ehCacheManagerFactoryBean = new EhCacheManagerFactoryBean();
        ehCacheManagerFactoryBean.setConfigLocation(new ClassPathResource("ehcache.xml"));
        return ehCacheManagerFactoryBean;
	}
	
	@Bean
	public EhCacheCacheManager cacheManager(net.sf.ehcache.CacheManager ehCacheManager) {
		EhCacheCacheManager cacheManager = new EhCacheCacheManager();
		cacheManager.setCacheManager(ehCacheManager);
		return cacheManager;
	}
}