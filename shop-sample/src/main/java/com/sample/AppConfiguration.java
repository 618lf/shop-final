package com.sample;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.shop.config.security.SecurityConfigurationSupport;
import com.shop.config.security.SecurityConfigurationSupport.SessionMode;
import com.tmt.Constants;
import com.tmt.core.entity.AutowireBean;
import com.tmt.core.utils.Lists;
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
	 * 安全配置
	 * 
	 * @return
	 */
	@Bean
	public SecurityConfigurationSupport securityConfiguration() {
		SecurityConfigurationSupport securityConfiguration = new SecurityConfigurationSupport();
		securityConfiguration.definition("/admin/validate/code = anon")
		        .definition("/admin/login = authc")
				.definition("/admin/logout = logout")
				.definition("/admin/** = user, roles[\"admin\"]")
				.definition("/** = user")
				.loginUrl("/admin/login").successUrl("/admin/")
				.unauthorizedUrl("/admin/login")
				.sessionMode(SessionMode.Session);
		return securityConfiguration;
	}
}