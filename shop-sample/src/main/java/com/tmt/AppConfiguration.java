package com.tmt;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.shop.config.security.SecurityConfigurationSupport;
import com.tmt.common.utils.Lists;
import com.tmt.system.service.TaskExecutor;
import com.tmt.task.AccessLogTask;
import com.tmt.task.SendEmailTask;
import com.tmt.task.UpdateHandlerTask;

/**
 * 项目配置
 * 
 * @author lifeng
 */
@Configuration
public class AppConfiguration {
	
	/**
	 * 批量设置定时任务
	 * @return
	 */
	@Bean
	public List<TaskExecutor> tasks() {
		List<TaskExecutor> tasks = Lists.newArrayList();
		tasks.add(new AccessLogTask());
		tasks.add(new SendEmailTask());
		tasks.add(new UpdateHandlerTask());
		return tasks;
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
		.loginUrl("/admin/login")
		.successUrl("/admin/")
		.unauthorizedUrl("/admin/login");
		return securityConfiguration;
	}
}