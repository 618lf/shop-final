package com.tmt.system.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.tmt.system.dao.TaskDao;
import com.tmt.system.entity.SystemConstant;
import com.tmt.system.entity.Task;
import com.tmt.system.entity.UpdateData;
import com.tmt.update.UpdateServiceFacade;

/**
 * 异步任务执行
 * -- 需要配知道配置文件中，才能激活
 * @author lifeng
 */
public class AsyncTaskCommandService implements TaskCommandServiceFacade{

	// 数据更新
	@Autowired
	private UpdateServiceFacade updateService;
	@Autowired
	private TaskDao taskDao;
	
	@Override
	public void remove(Long id) {
		Task taskTemp = this.taskDao.get(id);
		taskTemp.setOps("等待删除任务！");
		this._update(taskTemp, (byte)1); // 删除
		taskDao.update("updateOps", taskTemp);
	}

	@Override
	public void start(Long id) {
		Task taskTemp = this.taskDao.get(id);
		taskTemp.setOps("等待启动任务！");
		this._update(taskTemp, (byte)2); // 启动
		taskDao.update("updateOps", taskTemp);
	}

	@Override
	public void stop(Long id) {
		Task taskTemp = this.taskDao.get(id);
		taskTemp.setOps("等待停止任务！");
		this._update(taskTemp, (byte)3); // 停止
		taskDao.update("updateOps", taskTemp);
	}

	@Override
	public void pause(Long id) {
		Task taskTemp = this.taskDao.get(id);
		taskTemp.setOps("等待暂停任务！");
		this._update(taskTemp, (byte)4); // 暂停
		taskDao.update("updateOps", taskTemp);
	}

	@Override
	public void execute(Long id) {
		Task taskTemp = this.taskDao.get(id);
		taskTemp.setOps("等待执行任务！");
		this._update(taskTemp, (byte)5); // 立即执行
		taskDao.update("updateOps", taskTemp);
	}
	
	private void _update(Task goods, byte opt) {
		UpdateData updateData = new UpdateData();
		updateData.setId(goods.getId());
		updateData.setModule(SystemConstant.TASK_UPDATE);
		updateData.setOpt(opt);
		updateService.save(updateData);
	}
}
