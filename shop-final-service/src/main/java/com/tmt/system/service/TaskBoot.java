package com.tmt.system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.tmt.core.boot.AbstractBoot;
import com.tmt.core.email.EmailParam;
import com.tmt.core.utils.CacheUtils;
import com.tmt.system.entity.Task;
import com.tmt.system.entity.Task.TaskStatus;

/**
 * 定时任务加载器 1. 其实应该作为系统系统加载器（加载系统启动项）
 * 
 * @author root
 */
public class TaskBoot extends AbstractBoot {

	@Autowired
	private TaskService taskService;
	@Autowired
	private SiteService siteService;
	@Autowired
	private TaskCommandService taskCommandService;

	/**
	 * 初始化参数 并启动定时任务
	 */
	@Override
	public void init() {
		// 加载邮件配置
		EmailParam email = siteService.getSite().getEmailParam();
		CacheUtils.put(EmailParam.EMAIL_KEY, email);

		// 加载定时任务
		List<Task> tasks = taskService.queryRunAbleTasks();
		for (Task task : tasks) {
			task.setTaskStatus(TaskStatus.RUNABLE);
		}
		taskService.updateRunable(tasks);
		// 注册任务
		for (Task task : tasks) {
			taskCommandService.start(task.getId());
		}
	}

	/**
	 * 描述
	 */
	@Override
	public String describe() {
		return "定时任务加载项";
	}
}