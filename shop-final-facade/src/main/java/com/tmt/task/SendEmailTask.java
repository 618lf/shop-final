package com.tmt.task;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.tmt.common.email.SendEmailUtils;
import com.tmt.common.utils.Lists;
import com.tmt.common.utils.StringUtils;
import com.tmt.system.entity.EmailTask;
import com.tmt.system.entity.Task;
import com.tmt.system.service.EmailTaskServiceFacade;
import com.tmt.system.service.TaskExecutor;

/**
 * 邮件发送任务
 * @author lifeng
 */
public class SendEmailTask implements TaskExecutor{

	protected Logger logger = LoggerFactory.getLogger(SendEmailTask.class);
	
	@Autowired
	private EmailTaskServiceFacade emailTaskService;
	
	@Override
	public Boolean doTask(Task task) {
		try{
			List<EmailTask> emails = this.emailTaskService.querySendAbleEmails();
			List<EmailTask> store = Lists.newArrayList();
			if (emails != null && emails.size() != 0 ) {
				for(EmailTask email: emails) {
					Boolean bFlag = this.sendEmail(email);
					Byte times = email.getStatus();
					     times = times == null ? 0: times;
					if (bFlag) {
						email.setSendFlag(EmailTask.YES);
					} else {
						email.setSendFlag(++times);
					}
                    if (bFlag || times >= 3) {
                    	store.add(email);
					}
				}
				this.emailTaskService.storeTo(store);
				this.emailTaskService.update(emails);
			}
		}catch(Exception e){
			logger.error("发送邮件失败", e);
		}
		return null;
	}
	
	/**
	 * 发送邮件
	 * @param email
	 * @return
	 */
	private Boolean sendEmail(EmailTask email){
		try{
			String subject = email.getEmailTitle();
			String[] toArray = email.getEmailTo().split(",");
			String[] ccArray = StringUtils.isNotBlank(email.getEmailCc())?email.getEmailCc().split(","):null;
			String content = email.getEmailContent();
			return SendEmailUtils.sendNotificationMail(subject, toArray, ccArray, content);
		}catch(Exception e){}
		return Boolean.FALSE;
	}

	@Override
	public String getName() {
		return "邮件发送任务";
	}
}