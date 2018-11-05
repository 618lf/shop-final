package com.shop.config.wechat;

import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import com.tmt.wechat.DomainWechatOptionService;
import com.tmt.wechat.service.impl.WechatPayServiceImpl;

/**
 * 基础组件
 * 
 * @author lifeng
 */
@Configuration
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@Order(Ordered.HIGHEST_PRECEDENCE)
@ConditionalOnProperty(prefix = "spring.application", name = "enableWechat", matchIfMissing = true)
public class WechatAutoConfiguration {

	@Bean
	public DomainWechatOptionService wechatOptionService() {
		return new DomainWechatOptionService();
	}
	
	@Bean
	public WechatPayServiceImpl wechatPayService() {
		return new WechatPayServiceImpl();
	}
}
