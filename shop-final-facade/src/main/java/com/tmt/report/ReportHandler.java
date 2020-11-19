package com.tmt.report;

/**
 * 
 * @author lifeng
 *
 * @param <T>
 */
public interface ReportHandler<T> {

	/**
	 * 事件处理器
	 * @param event
	 */
	public void doHandler(T object);
	
	/**
	 * 下一个处理器
	 * @param handler
	 */
	public void setNextHandler(ReportHandler<T> handler);
}