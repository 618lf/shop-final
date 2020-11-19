package com.tmt.shop.state;

import com.tmt.shop.entity.OrderState;

/**
 * 事件处理器
 * @author root
 */
public interface StateHandler {
    
	/**
	 * 事件处理器
	 * -1 继续往下处理
	 *  0 处理失败（不会继续寻找处理器，下次再处理此状态）
	 *  1 处理成功（不需要在做处理）
	 * @param event
	 */
	public Byte doHandler(OrderState state);
	
	/**
	 * 下一个处理器
	 * @param handler
	 */
	public void setNextHandler(StateHandler handler);
}