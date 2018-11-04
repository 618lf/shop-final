package com.tmt.system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.common.persistence.BaseDao;
import com.tmt.common.service.BaseService;
import com.tmt.system.dao.EmailTaskDao;
import com.tmt.system.entity.EmailTask;
import com.tmt.system.service.EmailTaskServiceFacade;

/**
 * 定时任务服务类
 * @author lifeng
 */
@Service
public class EmailTaskService extends BaseService<EmailTask, Long> implements EmailTaskServiceFacade{

	@Autowired
	private EmailTaskDao emailTaskDao;
	
	@Override
	protected BaseDao<EmailTask, Long> getBaseDao() {
		return emailTaskDao;
	}
	
	/**
	 * 获取需要发送的邮件
	 * @return
	 */
	public List<EmailTask> querySendAbleEmails(){
		return this.queryForList("querySendAbleEmails", "");
	}
	
	/**
	 * 存储
	 * @param email
	 * @return
	 */
	@Transactional
	public Boolean store(EmailTask email){
		this.insert(email);
		return Boolean.TRUE;
	}
	
	/**
	 * 存储到
	 * @param emails
	 */
	@Transactional
	public void storeTo(List<EmailTask> emails){
		//删除
		this.batchDelete(emails);
		//批量插入到
		this.emailTaskDao.batchInsert(emails);
	}
	
	/**
	 * 存储
	 * @param email
	 * @return
	 */
	@Transactional
	public void update(List<EmailTask> emails){
		this.batchUpdate(emails);
	}
}