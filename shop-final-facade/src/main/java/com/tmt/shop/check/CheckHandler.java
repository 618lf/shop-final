package com.tmt.shop.check;

/**
 * 校验
 * @author lifeng
 */
public interface CheckHandler {

	/**
	 * 事件处理器
	 * @param event
	 */
	public OrderCheckResult doHandler(CheckWrap check);
	
	/**
	 * 下一个处理器
	 * @param handler
	 */
	public void setNextHandler(CheckHandler handler);
}