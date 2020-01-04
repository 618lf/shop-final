package com.tmt.core.lock;


/**
 * 锁 -- 有些资源需要锁来控制
 * 
 * @author lifeng
 */
public interface Lock {

	/**
	 * 加锁(acquireTimeout:默认10秒,lockTimeout:默认50秒)
	 */
	<T> T doHandler(Handler handler);

	/**
	 * 资源名称
	 * @return
	 */
	String getName();
}