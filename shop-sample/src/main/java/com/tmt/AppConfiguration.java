package com.tmt;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.shop.config.security.SecurityConfigurationSupport;
import com.tmt.common.entity.AutowireBean;
import com.tmt.common.utils.Lists;
import com.tmt.system.service.TaskExecutor;
import com.tmt.task.AccessLogTask;
import com.tmt.task.SendEmailTask;
import com.tmt.task.UpdateHandlerTask;
import com.tmt.update.UpdateHandler;
import com.tmt.update.UserOpsHandler;
import com.tmt.update.WechatOpsHandler;

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
	 * 处理器
	 * 
	 * @return
	 */
	@Bean
	public List<UpdateHandler> handlers() {
		List<UpdateHandler> handlers = Lists.newArrayList();
		handlers.add(new WechatOpsHandler());
		handlers.add(new UserOpsHandler());
		this.autowireBeans(handlers);
		return handlers;
	}

	/**
	 * 批量设置定时任务
	 * 
	 * @return
	 */
	@Bean
	public List<TaskExecutor> tasks(List<UpdateHandler> handlers) {
		List<TaskExecutor> tasks = Lists.newArrayList();
		tasks.add(new AccessLogTask());
		tasks.add(new SendEmailTask());
		tasks.add(new UpdateHandlerTask(handlers));
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
		securityConfiguration.definition("/admin/validate/code = anon").definition("/admin/login = authc")
				.loginUrl("/admin/login").successUrl("/admin/").unauthorizedUrl("/admin/login");
		return securityConfiguration;
	}
}