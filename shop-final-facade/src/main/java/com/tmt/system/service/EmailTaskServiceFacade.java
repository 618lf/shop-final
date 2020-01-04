package com.tmt.system.service;

import java.util.List;

import com.tmt.core.service.BaseServiceFacade;
import com.tmt.system.entity.EmailTask;

/**
 * 定时任务服务类
 * @author lifeng
 */
public interface EmailTaskServiceFacade extends BaseServiceFacade<EmailTask, Long>{

	/**
	 * 获取需要发送的邮件
	 * @return
	 */
	public List<EmailTask> querySendAbleEmails();
	
	/**
	 * 存储
	 * @param email
	 * @return
	 */
	public Boolean store(EmailTask email);
	
	/**
	 * 存储到
	 * @param emails
	 */
	public void storeTo(List<EmailTask> emails);
	
	/**
	 * 更新
	 * @param emails
	 */
	public void update(List<EmailTask> emails);
}