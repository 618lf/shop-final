package com.shop.config.searcher;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 属性设置
 * 
 * @author lifeng
 */
@ConfigurationProperties("spring.searcher")
public class SearcherProperties {

	private String storager;

	public String getStorager() {
		return storager;
	}

	public void setStorager(String storager) {
		this.storager = storager;
	}
}
