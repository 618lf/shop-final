package com.shop.config.cache;

import static com.shop.Application.APP_LOGGER;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import com.tmt.common.cache.ehcache.EhCacheCacheManager;

/**
 * 缓存配置
 * 
 * @author lifeng
 */
@Configuration
@EnableConfigurationProperties(CacheProperties.class)
@ConditionalOnProperty(prefix = "spring.cache", name = "enableEhCache", matchIfMissing = false)
public class EhCacheAutoConfiguration {

	@Autowired
	private CacheProperties properties;
	@Autowired
	private ResourceLoader resourceLoader;

	public EhCacheAutoConfiguration() {
		APP_LOGGER.debug("Loading EhCache");
	}

	@Bean
	public EhCacheManagerFactoryBean ehCacheManager() {
		EhCacheManagerFactoryBean ehCacheManagerFactoryBean = new EhCacheManagerFactoryBean();
		ehCacheManagerFactoryBean.setConfigLocation(loadConfigLocation());
		return ehCacheManagerFactoryBean;
	}

	/**
	 * 加载资源文件
	 * 
	 * @return
	 */
	private Resource loadConfigLocation() {
		return resourceLoader.getResource(properties.getConfigLocation());
	}

	@Bean
	public EhCacheCacheManager cacheManager(net.sf.ehcache.CacheManager ehCacheManager) {
		EhCacheCacheManager cacheManager = new EhCacheCacheManager();
		cacheManager.setCacheManager(ehCacheManager);
		return cacheManager;
	}
}