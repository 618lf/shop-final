package com.shop.config.storager;

import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import com.tmt.common.utils.storager.LocalStorager;

/**
 * 存储的配置
 * @author lifeng
 */
@Configuration
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE + 10)
@Order(Ordered.HIGHEST_PRECEDENCE + 10)
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
