package com.shop.config.cache;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import com.tmt.common.cache.redis.RedisCache;
import com.tmt.common.cache.redis.RedisCacheManager;
import com.tmt.common.cache.redis.factory.RedisConnectionFactory;
import com.tmt.common.utils.Lists;

import redis.clients.jedis.JedisPoolConfig;

/**
 * 缓存配置
 * 
 * @author lifeng
 */
@Configuration
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@Order(Ordered.HIGHEST_PRECEDENCE)
@EnableConfigurationProperties(CacheProperties.class)
@ConditionalOnProperty(prefix = "spring.cache", name = "enableRedis", matchIfMissing = false)
public class RedisAutoConfiguration {

	@Autowired
	private CacheProperties properties;

	@Bean
	public RedisConnectionFactory jedisConnectionFactory() {
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		RedisConnectionFactory connectionFactory = new RedisConnectionFactory();
		connectionFactory.setPoolConfig(poolConfig);
		poolConfig.setMaxTotal(properties.getMaxTotal());
		poolConfig.setMaxIdle(properties.getMaxIdle());
		poolConfig.setMaxWaitMillis(properties.getMaxWaitMillis());
		poolConfig.setTestOnBorrow(properties.getTestOnBorrow());

		connectionFactory.setHosts(properties.getHosts());
		connectionFactory.setPassword(properties.getPassword());
		connectionFactory.setTimeout(properties.getTimeout());
		return connectionFactory;
	}

	public RedisCache authCache() {
		RedisCache authCache = new RedisCache();
		authCache.setName("authorization");
		authCache.setPrex("#");
		authCache.setTimeToLive(-1);
		authCache.setTimeToIdle(1800);
		return authCache;
	}
	
	public RedisCache sysCache() {
		RedisCache authCache = new RedisCache();
		authCache.setName("sys");
		authCache.setPrex("#");
		authCache.setTimeToLive(-1);
		authCache.setTimeToIdle(-1);
		return authCache;
	}
	
	public RedisCache dictCache() {
		RedisCache authCache = new RedisCache();
		authCache.setName("dict");
		authCache.setPrex("#");
		authCache.setTimeToLive(-1);
		authCache.setTimeToIdle(-1);
		return authCache;
	}
	
	public RedisCache sessionCache() {
		RedisCache authCache = new RedisCache();
		authCache.setName("sess");
		authCache.setPrex("#");
		authCache.setTimeToLive(-1);
		authCache.setTimeToIdle(1800);
		return authCache;
	}
	
	@Bean
	public RedisCacheManager cacheManager() {
		RedisCacheManager cacheManager = new RedisCacheManager();
		List<RedisCache> caches = Lists.newArrayList();
		caches.add(authCache());
		caches.add(sysCache());
		caches.add(dictCache());
		caches.add(sessionCache());
		return cacheManager;
	}
}
