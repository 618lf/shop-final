package com.shop.config.schedule;

import java.util.Properties;

import org.quartz.Scheduler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.tmt.system.service.TaskBoot;

/**
 * 
 * 计划自动配置
 * 
 * @author lifeng
 */
@Configuration
@ConditionalOnClass(Scheduler.class)
@EnableConfigurationProperties(ScheduleProperties.class)
@ConditionalOnProperty(prefix = "spring.application", name = "enableSchedule", matchIfMissing = true)
public class ScheduleAutoConfiguration {

	/**
	 * 本地配置
	 * 
	 * @return
	 */
	@Bean
	public SchedulerFactoryBean localQuartzScheduler(ScheduleProperties properties) {
		SchedulerFactoryBean localQuartzScheduler = new SchedulerFactoryBean();
		Properties quartzProperties = new Properties();
		quartzProperties.setProperty("org.quartz.scheduler.instanceName", properties.getInstanceName());
		quartzProperties.setProperty("org.quartz.threadPool.threadCount", String.valueOf(properties.getThreadCount()));
		quartzProperties.setProperty("org.quartz.plugin.shutdownhook.class",
				"org.quartz.plugins.management.ShutdownHookPlugin");
		quartzProperties.setProperty("org.quartz.plugin.shutdownhook.cleanShutdown",
				String.valueOf(properties.getCleanShutdown()));
		quartzProperties.setProperty("org.quartz.threadPool.threadsInheritContextClassLoaderOfInitializingThread",
				String.valueOf(properties.getThreadsInheritContextClassLoaderOfInitializingThread()));
		localQuartzScheduler.setQuartzProperties(quartzProperties);
		localQuartzScheduler.setStartupDelay(properties.getStartupDelay());
		return localQuartzScheduler;
	}

	/**
	 * 定时任务启动管理
	 * 
	 * @return
	 */
	@Bean
	public TaskBoot taskBooter() {
		return new TaskBoot();
	}
}
