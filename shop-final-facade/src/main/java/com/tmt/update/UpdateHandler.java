package com.tmt.update;

import java.util.List;

import com.tmt.common.entity.AutowireBean;
import com.tmt.system.entity.UpdateData;

/**
 * 更新事件处理器
 * @author lifeng
 */
public interface UpdateHandler extends AutowireBean {
	
	/**
	 * 处理数据
	 * 
	 * @param moudle
	 * @return
	 */
	boolean handleFor(Byte moudle);

	/**
	 * 批量处理器（适合定时任务处理）
	 * @param event
	 */
	Boolean doHandler(List<UpdateData> datas);
	
	/**
	 * 单个的处理(适合消息队列)
	 * @param data
	 * @return
	 */
	Boolean doHandler(UpdateData data);
	
}