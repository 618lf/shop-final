package com.sample;

import java.io.File;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.shop.config.security.SecurityConfigurationSupport;
import com.tmt.Constants;
import com.tmt.common.entity.AutowireBean;
import com.tmt.common.utils.Lists;
import com.tmt.system.service.TaskExecutor;
import com.tmt.task.AccessLogTask;
import com.tmt.task.SendEmailTask;
import com.tmt.task.UpdateHandlerTask;
import com.tmt.update.UpdateHandler;
import com.tmt.update.UserOpsHandler;

import ch.qos.logback.classic.AsyncAppender;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.FileAppender;

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
		handlers.add(new UserOpsHandler());
		this.autowireBeans(handlers);
		return handlers;
	}

	/**
	 * 批量设置定时任务
	 * 
	 * @return
	 */
	@Bean(name = Constants.RUNNING_ABLE_TASKS)
	public List<TaskExecutor> tasks(List<UpdateHandler> handlers) {
		List<TaskExecutor> tasks = Lists.newArrayList();
		tasks.add(new AccessLogTask(getLogPath()));
		tasks.add(new SendEmailTask());
		tasks.add(new UpdateHandlerTask(handlers));
		this.autowireBeans(tasks);
		return tasks;
	}

	// log path
	private String getLogPath() {
		LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
		Appender<ILoggingEvent> appender = loggerContext.getLogger(Constants.SYS_ACCESS).getAppender("ASYNC_ACCESS");
		AsyncAppender asyncAppender = (AsyncAppender) appender;
		FileAppender<ILoggingEvent> fileAppender = (FileAppender<ILoggingEvent>) asyncAppender.getAppender("ACCESS");
		return new File(fileAppender.getFile()).getParent();
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
		.definition("/** = anon")
		.loginUrl("/admin/login")
		.successUrl("/admin/")
		.unauthorizedUrl("/admin/login");
		return securityConfiguration;
	}
}