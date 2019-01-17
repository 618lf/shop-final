package com.shop.config.security;

import static com.shop.Application.APP_LOGGER;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.shop.starter.ApplicationProperties;
import com.tmt.common.cache.CacheManager;
import com.tmt.common.security.SecurityFilter;
import com.tmt.common.security.SecurityFilterFactoryBean;
import com.tmt.common.security.mgt.RememberMeManager;
import com.tmt.common.security.mgt.support.DefaultRememberMeManager;
import com.tmt.common.security.mgt.support.DefaultSecurityManager;
import com.tmt.common.security.principal.PrincipalStrategy;
import com.tmt.common.security.principal.Session;
import com.tmt.common.security.principal.SessionRepository;
import com.tmt.common.security.principal.support.CookiePrincipalStrategy;
import com.tmt.common.security.principal.support.SessionRespositoryFactoryBean;
import com.tmt.common.web.filter.EncodingConvertFilter;
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
	public SessionRespositoryFactoryBean sessionRepository(CacheManager cacheManager) {
		SessionRespositoryFactoryBean sessionRepository = new SessionRespositoryFactoryBean();
		sessionRepository.setSessionTimeout(properties.getSecurity().getSessionTimeout());
		sessionRepository.setCacheManager(cacheManager);
		return sessionRepository;
	}

	@Bean
	public PrincipalStrategy principalStrategy(SessionRepository<? extends Session> sessionRepository) {
		CookiePrincipalStrategy principalStrategy = new CookiePrincipalStrategy();
		principalStrategy.setDomain(properties.getSecurity().getDomain());
		principalStrategy.setPath(properties.getSecurity().getPath());
		principalStrategy.setSessionRepository(sessionRepository);
		return principalStrategy;
	}

	@Bean
	public AuthenticationRealm authenticationRealm(CacheManager cacheManager) {
		AuthenticationRealm authenticationRealm = new AuthenticationRealm();
		authenticationRealm.setCacheName(properties.getSecurity().getCacheName());
		authenticationRealm.setCacheManager(cacheManager);
		return authenticationRealm;
	}

	@Bean
	public DefaultSecurityManager securityManager(AuthenticationRealm authenticationRealm,
			PrincipalStrategy principalStrategy, RememberMeManager rememberMeManager) {
		DefaultSecurityManager securityManager = new DefaultSecurityManager();
		securityManager.setPrincipalStrategy(principalStrategy);
		securityManager.setRealm(securityConfig.getRealm() == null ? authenticationRealm : securityConfig.getRealm());
		securityManager.setRememberMeManager(rememberMeManager);
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
		registrationBean.setOrder(FilterRegistrationBean.REQUEST_WRAPPER_FILTER_MAX_ORDER - 1);
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
		SecurityFilterFactoryBean securityFilter = new SecurityFilterFactoryBean();
		securityFilter.setSecurityManager(securityManager);
		securityFilter.setFilterChainDefinitionMap(securityConfig.getChains());
		securityFilter.setFilters(securityConfig.getFilters());
		securityFilter.setLoginUrl(securityConfig.getLoginUrl());
		securityFilter.setSuccessUrl(securityConfig.getSuccessUrl());
		securityFilter.setUnauthorizedUrl(securityConfig.getUnauthorizedUrl());
		FilterRegistrationBean<SecurityFilter> securityFilterBean = new FilterRegistrationBean<SecurityFilter>();
		securityFilterBean.setFilter(securityFilter.getObject());
		securityFilterBean.setUrlPatterns(properties.getSecurity().getUrlPatterns());
		securityFilterBean.setName("securityFilter");
		securityFilterBean.setOrder(FilterRegistrationBean.REQUEST_WRAPPER_FILTER_MAX_ORDER);
		return securityFilterBean;
	}
}
