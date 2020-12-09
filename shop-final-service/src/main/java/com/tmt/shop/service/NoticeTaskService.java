package com.tmt.shop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.core.codec.Digests;
import com.tmt.core.persistence.BaseDao;
import com.tmt.core.service.BaseService;
import com.tmt.shop.dao.NoticeTaskDao;
import com.tmt.shop.entity.NoticeTask;

/**
 * 通知任务 管理
 * @author 超级管理员
 * @date 2016-10-05
 */
@Service("shopNoticeTaskService")
public class NoticeTaskService extends BaseService<NoticeTask,Long> implements NoticeTaskServiceFacade{
	
	@Autowired
	private NoticeTaskDao noticeTaskDao;
	
	@Override
	protected BaseDao<NoticeTask, Long> getBaseDao() {
		return noticeTaskDao;
	}
	
	/**
	 * 保存
	 */
	@Transactional
	public void save(NoticeTask noticeTask) {
		noticeTask.setSource(Digests.md5(noticeTask.getMsg()));
		this.insert(noticeTask);
	}
	
    /**
     * 获取前几个 (会更新状态)
     * @param qc
     * @param size   
     * @return
     */
    @Override
    @Transactional
    public List<NoticeTask> queryNoticeAbles(int size) {
    	return noticeTaskDao.queryUpdateAbles(size);
    }

    /**
     *  成功发送后删除
     */
	@Override
	@Transactional
	public void sendSuccess(List<NoticeTask> noticeTasks) {
		this.noticeTaskDao.batchDelete(noticeTasks);
	}
	
    /**
     *  成功发送后删除
     */
	@Override
	@Transactional
	public void sendFail(List<NoticeTask> noticeTasks) {
		this.noticeTaskDao.updateReset(noticeTasks);
	}
}