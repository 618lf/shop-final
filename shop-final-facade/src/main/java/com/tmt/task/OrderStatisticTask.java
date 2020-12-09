package com.tmt.task;

import com.tmt.system.entity.Task;
import com.tmt.system.service.TaskExecutor;

/**
 *  每小时统计一次，
 *  订单统计
 *  销售额、订单报表
 * @author lifeng
 */
public class OrderStatisticTask implements TaskExecutor {

	@Override
	public Boolean doTask(Task arg0) {
		
		return null;
	}

	@Override
	public String getName() {
		return "订单统计";
	}
}
