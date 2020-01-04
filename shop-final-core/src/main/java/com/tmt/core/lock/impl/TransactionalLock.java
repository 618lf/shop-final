package com.tmt.core.lock.impl;

import com.tmt.core.exception.LockTimeOutException;
import com.tmt.core.lock.Handler;
import com.tmt.core.lock.Lock;
import com.tmt.core.utils.SpringContextHolder;

/**
 * 数据库的锁
 * 适用于无redis的情况
 * @author lifeng
 */
public class TransactionalLock implements Lock {

	private String name; 
	
	public TransactionalLock(String name) {
		this.name = name;
	}
	
	/**
	 * 在锁中执行这段代码
	 */
	@Override
	public <T> T doHandler(Handler handler) throws LockTimeOutException {
		
		// 如果有实现资源锁的service
		ResourceLockServiceFacade lockService = SpringContextHolder.getBean(ResourceLockServiceFacade.class);
		if (lockService == null) {
			throw new LockTimeOutException("no service ResourceLockServiceFacade");
		}
		
		// 在数据库的事务中执行这段代码
		return lockService.lock(this.getName(), handler);
	}

	@Override
	public String getName() {
		return name;
	}
}