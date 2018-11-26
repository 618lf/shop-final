package com.shop.config.cache;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.shop.config.security.SecurityAutoConfiguration;

/**
 * 缓存配置
 * 
 * @author lifeng
 */
@Configuration
@Import({EhCacheAutoConfiguration.class, RedisAutoConfiguration.class})
@ConditionalOnProperty(prefix = "spring.application", name = "enableCache", matchIfMissing = true)
@AutoConfigureBefore(SecurityAutoConfiguration.class)
public class CacheAutoConfiguration {}
