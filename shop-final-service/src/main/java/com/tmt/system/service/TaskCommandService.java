package com.tmt.system.service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.quartz.CronExpression;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.TriggerKey;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.Constants;
import com.tmt.common.utils.SpringContextHolder;
import com.tmt.common.utils.StringUtil3;
import com.tmt.system.dao.TaskDao;
import com.tmt.system.entity.Task;
import com.tmt.system.entity.Task.TaskStatus;

/**
 * 真实的任务执行命令
 * 
 * @author lifeng
 */
public class TaskCommandService {

	@Autowired
	private Scheduler scheduler;
	@Autowired
	private TaskDao taskDao;

	/**
	 * 删除任务
	 */
	@Transactional
	public void remove(Long id) {
		Task task = this.taskDao.get(id);
		task.setOps("任务删除失败");
		if (this.jobShutdown(task)) {
			taskDao.delete(task);
		} else {
			this.taskDao.update("updateOps", task);
		}
	}

	/**
	 * 启动任务
	 */
	@Transactional
	public void start(Long id) {
		Task task = this.taskDao.get(id);
		task.setTaskStatus(TaskStatus.FINISH);
		task.setOps("任务启动失败");
		if (isValidExpression(task)) {
			if (this.jobRunable(task)) {
				task.setTaskStatus(TaskStatus.RUNABLE);
				task.setOps("任务启动成功");
			}
		}
		this.taskDao.update("updateStatus", task);
	}

	/**
	 * 停止任务
	 */
	@Transactional
	public void stop(Long id) {
		Task task = this.taskDao.get(id);
		task.setOps("任务停止失败");
		if (this.jobShutdown(task)) {
			task.setTaskStatus(TaskStatus.FINISH);
			task.setOps("任务停止成功");
		}
		this.taskDao.update("updateStatus", task);
	}

	/**
	 * 暂停
	 */
	@Transactional
	public void pause(Long id) {
		Task task = this.taskDao.get(id);
		task.setOps("任务暂停失败");
		if (this.jobPause(task)) {
			task.setTaskStatus(TaskStatus.WAIT);
			task.setOps("任务暂停成功");
		}
		this.taskDao.update("updateStatus", task);
	}

	/**
	 * 执行中
	 */
	@Transactional
	public void execute(Long id) {
		Task task = this.taskDao.get(id);
		task.setOps("任务执行失败");
		if (task.getTaskStatus() != TaskStatus.RUNNING) {
			this.jobExecute(task);
			task.setOps("任务执行成功");
		}
		this.taskDao.update("updateOps", task);
	}

	/**
	 * 任务可执行(等待执行)
	 * 
	 * @param task
	 */
	public boolean jobRunable(Task task) {
		try {
			CronTriggerImpl trigger = (CronTriggerImpl) this.scheduler
					.getTrigger(TriggerKey.triggerKey(Constants.TASK_PREFIX + task.getId()));
			if (null == trigger) {
				JobDetail jobDetail = this.getJobDetail(task, false);
				if (jobDetail != null) {
					trigger = this.getCronTrigger(jobDetail, task);
					scheduler.scheduleJob(jobDetail, trigger);
					return true;
				}
			} else {
				trigger.setCronExpression(task.getCronExpression());
				scheduler.rescheduleJob(trigger.getKey(), trigger);
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	/**
	 * 任务不可执行（暂停）
	 * 
	 * @param task
	 */
	public boolean jobPause(Task task) {
		try {
			CronTriggerImpl trigger = (CronTriggerImpl) this.scheduler
					.getTrigger(TriggerKey.triggerKey(Constants.TASK_PREFIX + task.getId()));
			if (null == trigger) {
				return true;
			} else {
				scheduler.pauseTrigger(trigger.getKey());
				return true;
			}
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 删除
	 * 
	 * @return
	 */
	public boolean jobShutdown(Task task) {
		try {
			CronTriggerImpl trigger = (CronTriggerImpl) this.scheduler
					.getTrigger(TriggerKey.triggerKey(Constants.TASK_PREFIX + task.getId()));
			if (null == trigger) {
				return true;
			} else {
				scheduler.deleteJob(trigger.getJobKey());
				return true;
			}
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 立即执行 1. 如果之没任务，则创建之后执行一次，然后删除
	 * 
	 * @param task
	 * @return
	 */
	public boolean jobExecute(Task task) {
		try {
			CronTriggerImpl trigger = (CronTriggerImpl) this.scheduler
					.getTrigger(TriggerKey.triggerKey(Constants.TASK_PREFIX + task.getId()));
			if (null == trigger) {
				JobDetail jobDetail = this.getJobDetail(task, true);
				if (jobDetail != null) {
					scheduler.addJob(jobDetail, true);
					scheduler.triggerJob(jobDetail.getKey());
					scheduler.deleteJob(jobDetail.getKey());
					return true;
				}
			} else {
				scheduler.triggerJob(trigger.getJobKey());
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	// 构建执行器
	private CronTriggerImpl getCronTrigger(JobDetail jobDetail, Task task) throws ParseException {
		JobDataMap jobDataMap = new JobDataMap();
		Date startTime = new Date(System.currentTimeMillis());
		jobDataMap.put("jobDetail", jobDetail); // 固定写法
		CronTriggerImpl cti = new CronTriggerImpl();
		cti.setName(Constants.TASK_PREFIX + task.getId());
		cti.setGroup(Scheduler.DEFAULT_GROUP);
		cti.setJobKey(jobDetail.getKey());
		cti.setJobDataMap(jobDataMap);
		cti.setStartTime(startTime);
		cti.setCronExpression(task.getCronExpression());
		cti.setTimeZone(TimeZone.getDefault());
		cti.setPriority(1); // 优先级别
		return cti;
	}

	// 构建任务
	private JobDetail getJobDetail(Task task, boolean storeDurably) {
		TaskExecutor executor = null;
		if (StringUtil3.isBlank(task.getBusinessObject()) || (executor = this.getExecutor(task)) == null
				|| !this.isValidExpression(task)) {
			return null;
		}
		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put(Constants.JOB_TASK_KEY, task);
		jobDataMap.put(Constants.JOB_EXECUTOR_KEY, executor);
		Class<? extends TaskExecutorAdapter> jobClass = task.getConcurrent() ? TaskExecutorAdapter.class
				: StatefulTaskExecutorAdapter.class;
		JobBuilder builder = JobBuilder.newJob(jobClass);
		builder.usingJobData(jobDataMap);
		builder.withIdentity(new StringBuilder(Constants.TASK_PREFIX).append(task.getId()).toString());
		if (storeDurably) {
			builder.storeDurably();
		}
		return builder.build();
	}

	// 下一次执行的时间点(主要校验执行时间是否填写正确)
	private boolean isValidExpression(Task task) {
		return CronExpression.isValidExpression(task.getCronExpression());
	}

	// 具体的任务执行器
	private TaskExecutor getExecutor(Task task) {
		List<TaskExecutor> tasks = SpringContextHolder.getBean(Constants.RUNNING_ABLE_TASKS);
		for (TaskExecutor _task : tasks) {
			if (_task.getName().equals(task.getBusinessObject())) {
				return _task;
			}
		}
		return null;
	}
}