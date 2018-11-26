package com.shop.config;

import static com.shop.Application.APP_LOGGER;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.shop.booter.AppBooter;

/**
 * 系统配置
 * 
 * @author lifeng
 */
@Configuration
public class AppListenerAutoConfiguration {
	
	public AppListenerAutoConfiguration() {
		APP_LOGGER.debug("Loading App Booter");
	}

	@Bean
	public AppBooter appBooter() {
		return new AppBooter();
	}
}