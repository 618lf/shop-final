package com.tmt.update.impl;

import com.tmt.system.entity.UpdateData;
import com.tmt.update.UpdateHandler;

public abstract class AbstractUpdateHandler implements UpdateHandler {

	private UpdateHandler handler;

	/**
	 * 处理更新内容
	 */
	@Override
	public Boolean doHandler(UpdateData data) {
		
		/**
		 * 子类的处理
		 */
		if (this.doInnerHandler(data) != null) {
			return Boolean.TRUE;
		}
		
		/**
		 * 交给下一个来处理
		 */
		if (handler != null) {
			return handler.doHandler(data);
		}
		
		/**
		 * 没有处理的处理器了
		 */
		return null;
	}
	
	/**
	 * 子类实现
	 * @param data
	 * @return
	 */
	protected abstract Boolean doInnerHandler(UpdateData data);
	

	@Override
	public void setNextHandler(UpdateHandler handler) {
		this.handler = handler;
	}
}
