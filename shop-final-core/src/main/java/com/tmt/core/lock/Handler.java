package com.tmt.core.lock;

public interface Handler {

	/**
	 * 资源上执行的操作
	 * @author root
	 */
	public <T> T doHandle();
}
