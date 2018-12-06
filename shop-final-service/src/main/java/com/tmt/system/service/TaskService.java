package com.tmt.system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.Constants;
import com.tmt.common.entity.LabelVO;
import com.tmt.common.persistence.BaseDao;
import com.tmt.common.persistence.incrementer.IdGen;
import com.tmt.common.service.BaseService;
import com.tmt.common.utils.CacheUtils;
import com.tmt.common.utils.Lists;
import com.tmt.common.utils.SpringContextHolder;
import com.tmt.system.dao.TaskDao;
import com.tmt.system.entity.Task;
import com.tmt.system.entity.Task.TaskStatus;

/**
 * 定时任务执行，任务互不干扰
 * 
 * @ClassName: TaskService
 * @author 李锋
 * @date Jul 1, 2016 9:26:22 AM
 */
@Service
public class TaskService extends BaseService<Task, Long> implements TaskServiceFacade {

	@Autowired
	private TaskDao taskDao;
	@Autowired
	private TaskCommandService commandService;

	@Override
	protected BaseDao<Task, Long> getBaseDao() {
		return taskDao;
	}

	/**
	 * 仅仅保存信息 - 不会修改状态信息 1. 但如果任务是可执行状态（可执行和运行中）则需要刷新任务状态
	 * 
	 * @param task
	 * @return
	 */
	@Transactional
	public Long save(Task task) {
		if (IdGen.isInvalidId(task.getId())) {
			task.setTaskStatus(TaskStatus.NEW); // 默认是待启动状态
			this.insert(task);
		} else {
			Task taskTemp = this.get(task.getId()); // 保存不会改变状态
			taskTemp.setName(task.getName());
			taskTemp.setBusinessObject(task.getBusinessObject());
			taskTemp.setBusinessObjectName(task.getBusinessObjectName());
			taskTemp.setCronExpression(task.getCronExpression());
			taskTemp.setAllowExecuteCount(task.getAllowExecuteCount());

			if (taskTemp.getTaskStatus() == TaskStatus.RUNABLE || taskTemp.getTaskStatus() == TaskStatus.RUNNING) {
				commandService.start(task.getId());
			}
			this.update(taskTemp);
		}
		return task.getId();
	}

	/**
	 * 删除，并清空缓存 -- 使用Cache来保存
	 */
	@Transactional
	public void delete(List<Task> tasks) {
		for (Task task : tasks) {
			commandService.remove(task.getId());
		}
	}

	/**
	 * 启动任务
	 * 
	 * @param task
	 * @return
	 */
	@Transactional
	public void start(Task task) {
		commandService.start(task.getId());
	}

	/**
	 * 停止任务
	 * 
	 * @param task
	 * @return
	 */
	@Transactional
	public void stop(Task task) {
		commandService.stop(task.getId());
	}

	/**
	 * 现阶段值能执行一次周期任务,设置手工执行
	 * 
	 * @param tasks
	 */
	@Transactional
	public void pause(Task task) {
		commandService.pause(task.getId());
	}

	/**
	 * 现阶段只能执行一次周期任务,设置手工执行
	 * 
	 * @param tasks
	 */
	@Transactional
	public void execute(Task task) {
		commandService.execute(task.getId());
	}

	/**
	 * 更新为正在执行的状态
	 * 
	 * @param id
	 * @return
	 */
	@Transactional
	public Task preDoTask(Task t) {
		Task task = this.get(t.getId());
		if (task != null) {
			Task newTask = new Task();
			newTask.setId(t.getId());
			newTask.setTaskStatus(TaskStatus.RUNNING);
			this.update("updateStatus", newTask);
		}
		return task;
	}

	/**
	 * 执行结束后更新状态
	 * 
	 * @param id
	 */
	@Transactional
	public void postDoTask(Task task) {
		TaskStatus _back = task.getTaskStatus();
		task.setYetExecuteCount(task.getYetExecuteCount() == null ? 1 : (task.getYetExecuteCount() + 1));
		if (task.getAllowExecuteCount() != null && task.getYetExecuteCount() >= task.getAllowExecuteCount()) {
			task.setTaskStatus(TaskStatus.FINISH);
		} else {
			task.setTaskStatus(_back);
		}
		Task _new = this.get(task.getId());
		if (_new.getTaskStatus() != TaskStatus.RUNNING) { // 有其他并行任务修改了
			_back = _new.getTaskStatus();
		}
		task.setManualOperation(0);
		this.update("updateExecuteStatus", task);
	}

	/**
	 * 修改为可执行
	 * 
	 * @param tasks
	 */
	@Transactional
	public void updateRunable(List<Task> tasks) {
		this.batchUpdate("updateStatus", tasks);
	}

	/**
	 * 查询可执行的定时任务
	 * 
	 * @return
	 */
	public List<Task> queryRunAbleTasks() {
		return this.queryForList("findRunAbleTasks", "");
	}

	/**
	 * 业务配置的任务
	 */
	@Override
	public List<LabelVO> business() {
		List<LabelVO> tasks = CacheUtils.getSysCache().get(Constants.RUNNING_ABLE_TASKS);
		if (tasks == null) {
			tasks = Lists.newArrayList();
			List<TaskExecutor> _tasks = SpringContextHolder.getBean(Constants.RUNNING_ABLE_TASKS);
			for(TaskExecutor _task: _tasks) {
				String key = _task.getName();
				String label = _task.getName();
				tasks.add(LabelVO.newLabel(label, key));
			}
			// 加入缓存
			CacheUtils.getSysCache().put(Constants.RUNNING_ABLE_TASKS, tasks);
		}
		return tasks;
	}
}