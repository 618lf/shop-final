package com.shop.config.security;

import java.util.Map;

import javax.servlet.Filter;

import com.tmt.common.security.realm.Realm;
import com.tmt.common.security.utils.StringUtils;
import com.tmt.common.utils.Maps;

/**
 * 业务代码需要继承这个类
 * 
 * @author lifeng
 */
public class SecurityConfigurationSupport {

	private Map<String, Filter> filters = Maps.newOrderMap();
	private Realm realm;
	private Map<String, String> chains;
	private String loginUrl;
	private String successUrl;
	private String unauthorizedUrl; 
	
	public SecurityConfigurationSupport loginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
		return this;
	}
	public SecurityConfigurationSupport successUrl(String successUrl) {
		this.successUrl = successUrl;
		return this;
	}
	public SecurityConfigurationSupport unauthorizedUrl(String unauthorizedUrl) {
		this.unauthorizedUrl = unauthorizedUrl;
		return this;
	}
	public String getLoginUrl() {
		return loginUrl;
	}
	public String getSuccessUrl() {
		return successUrl;
	}
	public String getUnauthorizedUrl() {
		return unauthorizedUrl;
	}
	public Map<String, Filter> getFilters() {
		return filters;
	}
	public Realm getRealm() {
		return realm;
	}
	public Map<String, String> getChains() {
		return chains;
	}
	
	/**
	 * 设置域
	 * @param realm
	 */
	public SecurityConfigurationSupport realm(Realm realm) {
		this.realm = realm;
		return this;
	}
	
	/**
	 * 添加 filter
	 * @param name
	 * @param filter
	 * @return
	 */
	public SecurityConfigurationSupport addFilter(String name, Filter filter) {
		filters.put(name, filter);
		return this;
	}
	
	/**
	 * 配置 FilterChain
	 * @param line
	 * @return
	 */
	public SecurityConfigurationSupport definition(String line) {
		if (!StringUtils.hasText(line)) {
			return this;
		}
		String[] parts = StringUtils.split(line, '=');
		if (!(parts != null && parts.length == 2)) {
			return this;
		}
		String path = StringUtils.clean(parts[0]);
		String filter = StringUtils.clean(parts[1]);
		if (!(StringUtils.hasText(path) && StringUtils.hasText(filter))) {
			return this;
		}
		if (chains == null) {
			chains = Maps.newOrderMap();
		}
		chains.put(path, filter);
		return this;
	}
}
