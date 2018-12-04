package com.shop.config.cache;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 缓存
 * @author lifeng
 */
@ConfigurationProperties(prefix = "spring.cache")
public class CacheProperties {

	private Integer maxTotal = 5000;
	private Integer maxIdle = 3000;
	private Integer maxWaitMillis = -1;
	private Boolean testOnBorrow = false;
	private Integer timeout = 10000;
	private String hosts = "127.0.0.1:6379";
	private String password = "12345678....";
	private String configLocation = "classpath:ehcache.xml";
	
	public String getConfigLocation() {
		return configLocation;
	}
	public void setConfigLocation(String configLocation) {
		this.configLocation = configLocation;
	}
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
