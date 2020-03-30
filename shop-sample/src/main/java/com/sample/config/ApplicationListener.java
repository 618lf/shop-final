package com.sample.config;

import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 基于Servlet 的监听
 * 
 * @author lifeng
 */
@Configuration
public class ApplicationListener {

	@Bean
	public ServletListenerRegistrationBean<SessionListener> sessionListener() {
		return new ServletListenerRegistrationBean<SessionListener>(new SessionListener());
	}
}
