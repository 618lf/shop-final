package com.tmt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.shop.config.security.SecurityConfigurationSupport;

/**
 * 项目配置
 * 
 * @author lifeng
 */
@Configuration
public class AppConfiguration {

	/**
	 * 安全配置
	 * 
	 * @return
	 */
	@Bean
	public SecurityConfigurationSupport securityConfiguration() {
		SecurityConfigurationSupport securityConfiguration = new SecurityConfigurationSupport();
		securityConfiguration.definition("/admin/validate/code = anon")
		.definition("/admin/login = authc")
		.loginUrl("/admin/login")
		.successUrl("/admin/")
		.unauthorizedUrl("/admin/login");
		return securityConfiguration;
	}
}