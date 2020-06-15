package com.shop.config.security;

import static com.shop.Application.APP_LOGGER;

import java.util.Map;

import javax.servlet.Filter;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.filter.OrderedFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import com.shop.config.security.SecurityConfigurationSupport.SessionMode;
import com.shop.starter.ApplicationProperties;
import com.tmt.core.cache.CacheManager;
import com.tmt.core.cache.ehcache.EhCacheCacheManager;
import com.tmt.core.security.SecurityFilter;
import com.tmt.core.security.filter.AccessControllerFilter;
import com.tmt.core.security.filter.Nameable;
import com.tmt.core.security.filter.authc.AuthenticationFilter;
import com.tmt.core.security.filter.authc.LogoutFilter;
import com.tmt.core.security.filter.authz.AuthorizationFilter;
import com.tmt.core.security.mgt.FilterChainManager;
import com.tmt.core.security.mgt.FilterChainResolver;
import com.tmt.core.security.mgt.RememberMeManager;
import com.tmt.core.security.mgt.support.DefaultFilterChainManager;
import com.tmt.core.security.mgt.support.DefaultRememberMeManager;
import com.tmt.core.security.mgt.support.DefaultSecurityManager;
import com.tmt.core.security.mgt.support.PathMatchingFilterChainResolver;
import com.tmt.core.security.principal.PrincipalStrategy;
import com.tmt.core.security.principal.Session;
import com.tmt.core.security.principal.SessionRepository;
import com.tmt.core.security.principal.support.CookiePrincipalStrategy;
import com.tmt.core.security.principal.support.EhCacheSessionRepository;
import com.tmt.core.security.principal.support.HttpServletSessionRepository;
import com.tmt.core.security.principal.support.RedisSessionRepository;
import com.tmt.core.security.realm.Realm;
import com.tmt.core.security.utils.SecurityUtils;
import com.tmt.core.utils.StringUtils;
import com.tmt.core.web.filter.EncodingConvertFilter;
import com.tmt.system.realm.AuthenticationRealm;

/**
 * 安全配置
 * 
 * @author lifeng
 */
@Configuration
@ConditionalOnBean({ SecurityConfigurationSupport.class })
@EnableConfigurationProperties({ ApplicationProperties.class })
@ConditionalOnProperty(prefix = "spring.application", name = "enableSecurity", matchIfMissing = true)
public class SecurityAutoConfiguration {

	@Autowired
	private SecurityConfigurationSupport securityConfig;
	@Autowired
	private ApplicationProperties properties;

	public SecurityAutoConfiguration() {
		APP_LOGGER.debug("Loading Security");
	}

	@Bean
	public RememberMeManager rememberMeManager() {
		DefaultRememberMeManager rememberMeManager = new DefaultRememberMeManager();
		rememberMeManager.setDomain(properties.getSecurity().getDomain());
		rememberMeManager.setPath(properties.getSecurity().getPath());
		return rememberMeManager;
	}

	@Bean
	public PrincipalStrategy principalStrategy(CacheManager cacheManager) {
		CookiePrincipalStrategy principalStrategy = new CookiePrincipalStrategy();
		principalStrategy.setDomain(properties.getSecurity().getDomain());
		principalStrategy.setPath(properties.getSecurity().getPath());
		principalStrategy.setSessionRepository(getSessionRepository(cacheManager));
		return principalStrategy;
	}

	/**
	 * 选择一款session实现
	 */
	private SessionRepository<? extends Session> getSessionRepository(CacheManager cacheManager) {
		SessionRepository<? extends Session> sessionRepository = null;
		if (securityConfig.getSessionMode() == SessionMode.Cache) {
			if (cacheManager instanceof EhCacheCacheManager) {
				sessionRepository = new EhCacheSessionRepository(cacheManager);
			} else {
				sessionRepository = new RedisSessionRepository();
			}
		} else if (securityConfig.getSessionMode() == SessionMode.EhCache) {
			sessionRepository = new EhCacheSessionRepository(cacheManager);
		} else if (securityConfig.getSessionMode() == SessionMode.Redis) {
			sessionRepository = new RedisSessionRepository();
		} else if (securityConfig.getSessionMode() == SessionMode.Session) {
			sessionRepository = new HttpServletSessionRepository();
		}
		sessionRepository.setSessionTimeout(properties.getSecurity().getSessionTimeout());
		return sessionRepository;
	}

	@Bean
	@ConditionalOnMissingBean
	public AuthenticationRealm authenticationRealm(CacheManager cacheManager) {
		AuthenticationRealm authenticationRealm = new AuthenticationRealm();
		authenticationRealm.setCacheName(properties.getSecurity().getCacheName());
		authenticationRealm.setCacheManager(cacheManager);
		return authenticationRealm;
	}

	@Bean
	public DefaultSecurityManager securityManager(PrincipalStrategy principalStrategy,
			RememberMeManager rememberMeManager, ObjectProvider<Realm> realm) {
		Realm realmed = securityConfig.getRealm() == null ? realm.getIfAvailable() : securityConfig.getRealm();
		DefaultSecurityManager securityManager = new DefaultSecurityManager();
		securityManager.setPrincipalStrategy(principalStrategy);
		securityManager.setRealm(realmed);
		securityManager.setRememberMeManager(rememberMeManager);
		SecurityUtils.setSecurityManager(securityManager);
		return securityManager;
	}

	/**
	 * 字符过滤
	 * 
	 * @return
	 */
	@Bean
	public FilterRegistrationBean<EncodingConvertFilter> encodingConvertFilter() {
		EncodingConvertFilter encodingConvertFilter = new EncodingConvertFilter();
		FilterRegistrationBean<EncodingConvertFilter> registrationBean = new FilterRegistrationBean<EncodingConvertFilter>();
		registrationBean.setFilter(encodingConvertFilter);
		registrationBean.setUrlPatterns(properties.getSecurity().getUrlPatterns());
		registrationBean.setName("encodingConvertFilter");
		registrationBean.setOrder(OrderedFilter.REQUEST_WRAPPER_FILTER_MAX_ORDER - 1);
		return registrationBean;
	}

	/**
	 * 安全的配置
	 * 
	 * @param securityManager
	 * @return
	 * @throws Exception
	 */
	@Bean
	public FilterRegistrationBean<SecurityFilter> securityFilter(DefaultSecurityManager securityManager)
			throws Exception {
		FilterRegistrationBean<SecurityFilter> securityFilterBean = new FilterRegistrationBean<SecurityFilter>();
		securityFilterBean.setFilter(this.createSecurityFilter(securityManager));
		securityFilterBean.setUrlPatterns(properties.getSecurity().getUrlPatterns());
		securityFilterBean.setName("securityFilter");
		securityFilterBean.setOrder(OrderedFilter.REQUEST_WRAPPER_FILTER_MAX_ORDER);
		return securityFilterBean;
	}

	/**
	 * 创建一个安全过滤器
	 * 
	 * @param securityManager
	 * @return
	 */
	private SecurityFilter createSecurityFilter(DefaultSecurityManager securityManager) {
		FilterChainManager chainManager = createFilterChainManager();
		FilterChainResolver chainResolver = new PathMatchingFilterChainResolver();
		chainResolver.setFilterChainManager(chainManager);
		return new SecurityFilter(securityManager, chainResolver);
	}

	private FilterChainManager createFilterChainManager() {
		FilterChainManager chainManager = new DefaultFilterChainManager();
		Map<String, Filter> defaultFilters = chainManager.getFilters();
		// apply global settings if necessary:
		for (Filter filter : defaultFilters.values()) {
			applyGlobalPropertiesIfNecessary(filter);
		}

		// Apply the acquired and/or configured filters:
		Map<String, Filter> filters = securityConfig.getFilters();
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

		// build up the chains:
		Map<String, String> chains = securityConfig.getChains();
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
		String loginUrl = securityConfig.getLoginUrl();
		if (StringUtils.hasText(loginUrl) && (filter instanceof AccessControllerFilter)) {
			AccessControllerFilter acFilter = (AccessControllerFilter) filter;
			acFilter.setLoginUrl(loginUrl);
		}

		// 登录成功的地址
		String successUrl = securityConfig.getSuccessUrl();
		if (StringUtils.hasText(successUrl) && (filter instanceof AuthenticationFilter)) {
			AuthenticationFilter authcFilter = (AuthenticationFilter) filter;
			authcFilter.setSuccessUrl(successUrl);
		}

		// 无权限的地址
		String unauthorizedUrl = securityConfig.getUnauthorizedUrl();
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
