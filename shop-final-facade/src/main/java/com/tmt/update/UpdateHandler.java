package com.tmt.update;

import java.util.List;

import com.tmt.system.entity.UpdateData;

/**
 * 更新事件处理器
 * @author lifeng
 */
public interface UpdateHandler {

	/**
	 * 批量处理器（适合定时任务处理）
	 * @param event
	 */
	public Boolean doHandler(List<UpdateData> datas);
	
	/**
	 * 单个的处理(适合消息队列)
	 * @param data
	 * @return
	 */
	public Boolean doHandler(UpdateData data);
	
	/**
	 * 下一个处理器
	 * @param handler
	 */
	public void setNextHandler(UpdateHandler handler); 
}