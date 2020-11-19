package com.tmt.shop.service;

import java.util.List;

import com.tmt.core.service.BaseServiceFacade;
import com.tmt.shop.entity.NoticeTask;

public interface NoticeTaskServiceFacade extends BaseServiceFacade<NoticeTask,Long> {

	/**
	 * 保存
	 */
	public void save(NoticeTask noticeTask);
	
	/**
	 * 删除 - 成功发送
	 */
	public void sendSuccess(List<NoticeTask> noticeTasks);
	
	/**
	 * 重置 - 未成功发送
	 */
	public void sendFail(List<NoticeTask> noticeTasks);
	
    /**
     * 获取前几个 (会更新状态)
     * @param qc
     * @param size   
     * @return
     */
    public List<NoticeTask> queryNoticeAbles(int size);
}
