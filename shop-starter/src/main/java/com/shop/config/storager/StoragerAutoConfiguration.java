package com.shop.config.storager;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.tmt.common.utils.storager.LocalStorager;

/**
 * 存储的配置
 * @author lifeng
 */
@Configuration
@EnableConfigurationProperties(StoragerProperties.class)
@ConditionalOnProperty(prefix = "spring.application", name = "enableStorager", matchIfMissing = true)
public class StoragerAutoConfiguration {

	/**
	 * 存储
	 * 
	 * @param properties
	 * @return
	 */
	@Bean
	public LocalStorager storager(StoragerProperties properties) {
		LocalStorager storager = new LocalStorager();
		storager.setStoragePath(properties.getStoragePath());
		storager.setUrlPath(properties.getUrlPath());
		storager.setDomain(properties.getDomain());
		return storager;
	}
}
