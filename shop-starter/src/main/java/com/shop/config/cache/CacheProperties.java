package com.shop.config.cache;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 缓存
 * @author lifeng
 */
@ConfigurationProperties(prefix = "spring.cache")
public class CacheProperties {

	private Integer maxTotal;
	private Integer maxIdle;
	private Integer maxWaitMillis;
	private Boolean testOnBorrow;
	private Integer timeout;
	
	private String hosts;
	private String password;
	
	public Integer getMaxTotal() {
		return maxTotal;
	}
	public void setMaxTotal(Integer maxTotal) {
		this.maxTotal = maxTotal;
	}
	public Integer getMaxIdle() {
		return maxIdle;
	}
	public void setMaxIdle(Integer maxIdle) {
		this.maxIdle = maxIdle;
	}
	public Integer getMaxWaitMillis() {
		return maxWaitMillis;
	}
	public void setMaxWaitMillis(Integer maxWaitMillis) {
		this.maxWaitMillis = maxWaitMillis;
	}
	public Boolean getTestOnBorrow() {
		return testOnBorrow;
	}
	public void setTestOnBorrow(Boolean testOnBorrow) {
		this.testOnBorrow = testOnBorrow;
	}
	public Integer getTimeout() {
		return timeout;
	}
	public void setTimeout(Integer timeout) {
		this.timeout = timeout;
	}
	public String getHosts() {
		return hosts;
	}
	public void setHosts(String hosts) {
		this.hosts = hosts;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
