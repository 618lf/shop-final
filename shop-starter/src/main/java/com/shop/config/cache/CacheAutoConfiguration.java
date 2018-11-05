package com.shop.config.cache;

import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * 缓存配置
 * 
 * @author lifeng
 */
@Configuration
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@Order(Ordered.HIGHEST_PRECEDENCE)
@Import({EhCacheAutoConfiguration.class, RedisAutoConfiguration.class})
@ConditionalOnProperty(prefix = "spring.application", name = "enableCache", matchIfMissing = true)
public class CacheAutoConfiguration {}
