package com.tmt.system.service;

/**
 * 任务命令
 * @author lifeng
 */
public interface TaskCommandServiceFacade {

	/**
	 * 启动任务
	 * @param task
	 * @return
	 */
	public void remove(Long task);
	
	/**
	 * 启动任务
	 * @param task
	 * @return
	 */
	public void start(Long task);
	
	/**
	 * 启动任务
	 * @param task
	 * @return
	 */
	public void stop(Long task);
	
	/**
	 * 现阶段值能执行一次周期任务,设置手工执行
	 * @param tasks
	 */
	public void pause(Long task);
	
	/**
	 * 现阶段值能执行一次周期任务,设置手工执行
	 * @param tasks
	 */
	public void execute(Long task);
	
}