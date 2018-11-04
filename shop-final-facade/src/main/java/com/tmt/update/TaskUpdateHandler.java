package com.tmt.update;

import java.util.List;

import com.tmt.common.utils.SpringContextHolder;
import com.tmt.system.entity.SystemConstant;
import com.tmt.system.entity.UpdateData;
import com.tmt.system.service.TaskCommandServiceFacade;
import com.tmt.update.impl.AbstractUpdateHandler;

/**
 * 任务更新
 * @author lifeng
 */
public class TaskUpdateHandler extends AbstractUpdateHandler{

	private TaskCommandServiceFacade taskCommandService;
	
	public TaskUpdateHandler() {
		taskCommandService = SpringContextHolder.getBean(TaskCommandServiceFacade.class);
	}
	
	@Override
	public Boolean doHandler(List<UpdateData> datas) {
		if (datas == null || datas.isEmpty()) {
			return Boolean.TRUE;
		}
		for(UpdateData data: datas) {
			if (data.getOpt() == 1) {
				taskCommandService.remove(data.getId());
			} else if(data.getOpt() == 2) {
				taskCommandService.start(data.getId());
			} else if(data.getOpt() == 3) {
				taskCommandService.stop(data.getId());
			} else if(data.getOpt() == 4) {
				taskCommandService.pause(data.getId());
			} else if(data.getOpt() == 5) {
				taskCommandService.execute(data.getId());
			}
		}
		return null;
	}

	/**
	 * 执行
	 */
	@Override
	protected Boolean doInnerHandler(UpdateData data) {
		if (SystemConstant.TASK_UPDATE == data.getModule()) {
			if (data.getOpt() == 1) {
				taskCommandService.remove(data.getId());
			} else if(data.getOpt() == 2) {
				taskCommandService.start(data.getId());
			} else if(data.getOpt() == 3) {
				taskCommandService.stop(data.getId());
			} else if(data.getOpt() == 4) {
				taskCommandService.pause(data.getId());
			} else if(data.getOpt() == 5) {
				taskCommandService.execute(data.getId());
			}
			return Boolean.TRUE;
		}
		return null;
	}
}