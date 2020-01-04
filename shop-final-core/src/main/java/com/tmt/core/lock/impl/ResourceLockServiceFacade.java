package com.tmt.core.lock.impl;

import com.tmt.core.lock.Handler;

/**
 * 全局资源锁
 * @author root
 */
public interface ResourceLockServiceFacade {

	/**
	 * 锁住记录,过程中不需要事务
	 * @param resource
	 */
	public <T> T lock(String resource, Handler handler);
}