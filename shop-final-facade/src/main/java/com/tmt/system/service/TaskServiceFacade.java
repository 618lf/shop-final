package com.tmt.system.service;

import java.util.List;

import com.tmt.common.entity.LabelVO;
import com.tmt.common.service.BaseServiceFacade;
import com.tmt.system.entity.Task;

/**
 * 解决一些小的临时任务， 避免配置过多的任务
 * 定时任务只在一台主服务器上执行。（网上建议，最好是单独的服务器来处理定时任务的线程）
 * @author lifeng
 */
public interface TaskServiceFacade extends BaseServiceFacade<Task, Long>{

	/**
	 * 仅仅保存信息 - 不会修改状态信息
	 * 1. 但如果任务是可执行状态（可执行和运行中）则需要刷新任务状态
	 * @param task
	 * @return
	 */
	public Long save(Task task);
	
	/**
	 * 删除，并清空缓存 -- 使用Cache来保存
	 */
	public void delete(List<Task> tasks);
	
	/**
	 * 启动任务
	 * @param task
	 * @return
	 */
	public void start(Task task);
	
	/**
	 * 启动任务
	 * @param task
	 * @return
	 */
	public void stop(Task task);
	
	/**
	 * 现阶段值能执行一次周期任务,设置手工执行
	 * @param tasks
	 */
	public void pause(Task task);
	
	/**
	 * 现阶段值能执行一次周期任务,设置手工执行
	 * @param tasks
	 */
	public void execute(Task task);
	
	/**
	 * 业务任务
	 * @param tasks
	 */
	public List<LabelVO> business();
	
	/**
	 * 任务执行前
	 * @param t
	 * @return
	 */
	public Task preDoTask(Task t);
	
	/**
	 * 任务执行后
	 * @param task
	 */
	public void postDoTask(Task task);
}