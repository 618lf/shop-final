package com.tmt.shop.event;

import com.tmt.shop.entity.OrderEvent;

/**
 * 事件处理器
 * @author root
 */
public interface EventHandler {
    
	/**
	 * 事件处理器
	 * @param event
	 */
	public Boolean doHandler(OrderEvent event);
	
	/**
	 * 下一个处理器
	 * @param handler
	 */
	public void setNextHandler(EventHandler handler);
}