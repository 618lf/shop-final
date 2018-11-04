package com.tmt.common.security;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

import javax.servlet.Filter;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.CollectionUtils;

import com.tmt.common.security.filter.AccessControllerFilter;
import com.tmt.common.security.filter.Nameable;
import com.tmt.common.security.filter.authc.AuthenticationFilter;
import com.tmt.common.security.filter.authc.LogoutFilter;
import com.tmt.common.security.filter.authz.AuthorizationFilter;
import com.tmt.common.security.mgt.FilterChainManager;
import com.tmt.common.security.mgt.FilterChainResolver;
import com.tmt.common.security.mgt.SecurityManager;
import com.tmt.common.security.mgt.support.DefaultFilterChainManager;
import com.tmt.common.security.mgt.support.DefaultSecurityManager;
import com.tmt.common.security.mgt.support.PathMatchingFilterChainResolver;
import com.tmt.common.security.utils.SecurityUtils;
import com.tmt.common.security.utils.StringUtils;
import com.tmt.common.utils.Maps;

/**
 * 初始化安全filter
 * 
 * @author lifeng
 */
public class SecurityFilterFactoryBean implements FactoryBean<SecurityFilter>, BeanPostProcessor {

	private SecurityManager securityManager;
	private Map<String, Filter> filters;
	private Map<String, String> filterChainDefinitionMap;
	private String loginUrl;
	private String successUrl;
	private String unauthorizedUrl;
	private SecurityFilter instance;
	
	public SecurityFilterFactoryBean() {
		 this.filters = new LinkedHashMap<String, Filter>();
	     this.filterChainDefinitionMap = new LinkedHashMap<String, String>();
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName)
			throws BeansException {
		if (bean instanceof Filter) {
            Filter filter = (Filter) bean;
            getFilters().put(beanName, filter);
        } 
        return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName)
			throws BeansException {
		return bean;
	}

	@Override
	public SecurityFilter getObject() throws Exception {
		if (instance == null) {
			instance = createInstance();
		}
		return instance;
	}

	@Override
	public Class<?> getObjectType() {
		return SecurityFilter.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
	
	public Map<String, String> getFilterChainDefinitionMap() {
		return filterChainDefinitionMap;
	}

	public void setFilterChainDefinitionMap(Map<String, String> filterChainDefinitionMap) {
		this.filterChainDefinitionMap = filterChainDefinitionMap;
	}

	public Map<String, Filter> getFilters() {
		return filters;
	}

	public void setFilters(Map<String, Filter> filters) {
		this.filters = filters;
	}

	public SecurityManager getSecurityManager() {
		return securityManager;
	}

	public void setSecurityManager(SecurityManager securityManager) {
		this.securityManager = securityManager;
	}

	public String getLoginUrl() {
		return loginUrl;
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	public String getSuccessUrl() {
		return successUrl;
	}

	public void setSuccessUrl(String successUrl) {
		this.successUrl = successUrl;
	}

	public String getUnauthorizedUrl() {
		return unauthorizedUrl;
	}

	public void setUnauthorizedUrl(String unauthorizedUrl) {
		this.unauthorizedUrl = unauthorizedUrl;
	}
	
	/**
	 * url 定义的配置
	 * url 配置是有顺序的
	 * @param definitions
	 */
	public void setFilterChainDefinitions(String definitions) {
		Map<String, String> filterChainDefinitionMap = Maps.newOrderMap();
 		Scanner scanner = new Scanner(definitions);
		while (scanner.hasNextLine()) {
            String line = StringUtils.clean(scanner.nextLine());
            if (!StringUtils.hasText(line)) {continue;}
            String[] parts = StringUtils.split(line, '=');
            if (!(parts != null && parts.length == 2)) {continue;}
            String path = StringUtils.clean(parts[0]);
            String filter = StringUtils.clean(parts[1]);
            if (!(StringUtils.hasText(path) && StringUtils.hasText(filter))) {continue;}
            filterChainDefinitionMap.put(path, filter);
		}
		IOUtils.closeQuietly(scanner);
		
		// 设置
		this.setFilterChainDefinitionMap(filterChainDefinitionMap);
	}

	private SecurityFilter createInstance() {
		SecurityManager securityManager = getSecurityManager();
		if (securityManager == null) {
			securityManager = new DefaultSecurityManager();
		}
		
		// 设置为全局
		SecurityUtils.setSecurityManager(securityManager);
		
		FilterChainManager chainManager = createFilterChainManager();
		FilterChainResolver chainResolver = new PathMatchingFilterChainResolver();
		chainResolver.setFilterChainManager(chainManager);
		return new SecurityFilter(securityManager, chainResolver);
	}
	
	private FilterChainManager createFilterChainManager() {
		FilterChainManager chainManager = new DefaultFilterChainManager();
		Map<String, Filter> defaultFilters = chainManager.getFilters();
        //apply global settings if necessary:
        for (Filter filter : defaultFilters.values()) {
            applyGlobalPropertiesIfNecessary(filter);
        }
		
		//Apply the acquired and/or configured filters:
        Map<String, Filter> filters = getFilters();
        if (!CollectionUtils.isEmpty(filters)) {
            for (Map.Entry<String, Filter> entry : filters.entrySet()) {
                String name = entry.getKey();
                Filter filter = entry.getValue();
                applyGlobalPropertiesIfNecessary(filter);
                if (filter instanceof Nameable) {
                    ((Nameable) filter).setName(name);
                }
                chainManager.addFilter(name, filter, false);
            }
        }
        
        //build up the chains:
        Map<String, String> chains = getFilterChainDefinitionMap();
        if (!CollectionUtils.isEmpty(chains)) {
            for (Map.Entry<String, String> entry : chains.entrySet()) {
                String url = entry.getKey();
                String chainDefinition = entry.getValue();
                chainManager.createChain(url, chainDefinition);
            }
        }
        
        return chainManager;
	}
	
	private void applyGlobalPropertiesIfNecessary(Filter filter) {
		
		// 将登录url 设置到
		String loginUrl = getLoginUrl();
        if (StringUtils.hasText(loginUrl) && (filter instanceof AccessControllerFilter)) {
        	AccessControllerFilter acFilter = (AccessControllerFilter) filter;
        	acFilter.setLoginUrl(loginUrl);
        }
        
        // 登录成功的地址
        String successUrl = getSuccessUrl();
        if (StringUtils.hasText(successUrl) && (filter instanceof AuthenticationFilter)) {
            AuthenticationFilter authcFilter = (AuthenticationFilter) filter;
            authcFilter.setSuccessUrl(successUrl);
        }
        
        // 无权限的地址
        String unauthorizedUrl = getUnauthorizedUrl();
        if (StringUtils.hasText(unauthorizedUrl) && (filter instanceof AuthorizationFilter)) {
            AuthorizationFilter authzFilter = (AuthorizationFilter) filter;
            authzFilter.setUnauthorizedUrl(unauthorizedUrl);
        }
        
        // 退出系统后的地址
        if (StringUtils.hasText(loginUrl) && (filter instanceof LogoutFilter)) {
        	LogoutFilter authzFilter = (LogoutFilter) filter;
            authzFilter.setRedirectUrl(loginUrl);
        }
    }
}