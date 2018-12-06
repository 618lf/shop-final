package com.tmt.system.service;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tmt.Constants;
import com.tmt.common.utils.SpringContextHolder;
import com.tmt.system.entity.Task;
import com.tmt.system.entity.TaskProgress;

/**
 * 任务执行适配器 1. 立即执行是计划之外的执行,不会改变计划的下次执行时间 2.
 * 设置的执行时间和上次执行时间比较（内部保留的），如果已过则不执行，未过则执行 同时判断是否应该执行。
 * 
 * @author root
 */
public class TaskExecutorAdapter implements Job {

	private Logger logger = LoggerFactory.getLogger(TaskExecutorAdapter.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		Task _task = (Task) context.getMergedJobDataMap().get(Constants.JOB_TASK_KEY);
		TaskExecutor target = (TaskExecutor) context.getMergedJobDataMap().get(Constants.JOB_EXECUTOR_KEY);
		TaskServiceFacade taskService = SpringContextHolder.getBean(TaskServiceFacade.class);
		Task task = taskService.preDoTask(_task);
		if (task != null && target != null) {
			TaskProgress.removeTaskProgress(task.getId());
			try {
				target.doTask(task);
			} catch (Exception e) {
				logger.error("任务执行失败:", e);
			}
			task.setPreExecuteTime(context.getFireTime());
			task.setNextExecuteTime(
					context.getNextFireTime() != null ? context.getNextFireTime() : task.getNextExecuteTime());
			taskService.postDoTask(task);
			TaskProgress.removeTaskProgress(task.getId());
		}
	}
}