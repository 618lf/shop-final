package com.shop.config.cache;

import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;

import com.tmt.common.cache.ehcache.EhCacheCacheManager;

/**
 * 缓存配置
 * 
 * @author lifeng
 */
@Configuration
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@Order(Ordered.HIGHEST_PRECEDENCE)
@ConditionalOnProperty(prefix = "spring.cache", name = "enableEhCache", matchIfMissing = false)
public class EhCacheAutoConfiguration {

	/**
	 * 使用这个配置可能还好一点
	 * @return
	 */
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
		return new EhCacheCacheManager();
	}
}
