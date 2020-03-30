package com.sample;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.filter.OrderedFilter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sample.sso.ExternalSsoFilter;
import com.sample.sso.SsoAuthenticationRealm;
import com.shop.config.security.SecurityConfigurationSupport;
import com.shop.config.security.SecurityConfigurationSupport.SessionMode;
import com.shop.starter.ApplicationProperties;
import com.tmt.Constants;
import com.tmt.core.cache.CacheManager;
import com.tmt.core.entity.AutowireBean;
import com.tmt.core.utils.Lists;
import com.tmt.system.realm.AuthenticationRealm;
import com.tmt.system.service.TaskExecutor;
import com.tmt.task.AccessLogTask;
import com.tmt.task.SendEmailTask;

/**
 * 项目配置
 * 
 * @author lifeng
 */
@Configuration
public class AppConfiguration {

	@Autowired
	private ApplicationContext context;
	@Autowired
	private ApplicationProperties properties;

	/**
	 * 批量设置定时任务
	 * 
	 * @return
	 */
	@Bean(name = Constants.RUNNING_ABLE_TASKS)
	public List<TaskExecutor> tasks() {
		List<TaskExecutor> tasks = Lists.newArrayList();
		tasks.add(new AccessLogTask());
		tasks.add(new SendEmailTask());
		this.autowireBeans(tasks);
		return tasks;
	}

	// 自动添加依赖
	private void autowireBeans(List<? extends AutowireBean> beans) {
		for (Object bean : beans) {
			context.getAutowireCapableBeanFactory().autowireBean(bean);
		}
	}
	
	/**
	 * 默认的数据域
	 * 
	 * @param properties
	 * @param cacheManager
	 * @return
	 */
	@Bean
	public AuthenticationRealm authenticationRealm(ApplicationProperties properties, CacheManager cacheManager) {
		AuthenticationRealm authenticationRealm = new SsoAuthenticationRealm();
		authenticationRealm.setCacheName(properties.getSecurity().getCacheName());
		authenticationRealm.setCacheManager(cacheManager);
		return authenticationRealm;
	}
	
	/**
	 * 安全配置
	 * 
	 * @return
	 */
	@Bean
	public SecurityConfigurationSupport securityConfiguration(AuthenticationRealm authenticationRealm) {
		SecurityConfigurationSupport securityConfiguration = new SecurityConfigurationSupport();
		securityConfiguration.definition("/admin/validate/code = anon")
		        .definition("/admin/login = anon")
				.definition("/admin/logout = anon")
				.definition("/admin/** = anon")
				.definition("/** = anon")
				.loginUrl("/admin/login").successUrl("/admin/")
				.unauthorizedUrl("/admin/login")
				.sessionMode(SessionMode.Session);
		return securityConfiguration.realm(authenticationRealm);
	}
	
	/**
	 * SSO 过滤器，保证在安全配置后执行即可
	 * 
	 * @return
	 */
	@Bean
	public FilterRegistrationBean<ExternalSsoFilter> passportFilter() {
		FilterRegistrationBean<ExternalSsoFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new ExternalSsoFilter());
		registrationBean.setUrlPatterns(properties.getSecurity().getUrlPatterns());
		registrationBean.setName("passportFilter");
		registrationBean.setOrder(OrderedFilter.REQUEST_WRAPPER_FILTER_MAX_ORDER + 1);
		return registrationBean;
	}
}