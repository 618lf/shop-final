package com.tmt.system.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tmt.common.entity.AutowireBean;
import com.tmt.system.entity.Task;

/**
 * 定时任务任务执行器
 * @author lifeng
 */
public interface TaskExecutor extends AutowireBean{
    
	/**
	 * 日志记录
	 */
	public static Logger logger = LoggerFactory.getLogger(TaskExecutor.class);
	
	/**
	 * 执行类调用的方法
	 * @return
	 */
	public abstract Boolean doTask(Task task);
	
	/**
	 * 显示名称
	 * @return
	 */
	public abstract String getName();
}
