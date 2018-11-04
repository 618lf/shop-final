package com.tmt.system.utils;

import com.tmt.common.persistence.incrementer.IdGen;
import com.tmt.common.utils.SpringContextHolder;
import com.tmt.system.entity.EmailTask;
import com.tmt.system.service.EmailTaskServiceFacade;

/**
 * 系统使用异步发送邮件功能
 * @author root
 */
public class EmailUtils {

	private static EmailTaskServiceFacade emailTaskService = SpringContextHolder.getBean(EmailTaskServiceFacade.class);
	
	/**
	 * 发送邮件
	 * @param title
	 * @param to
	 * @param content
	 */
	public static void sendEmail(String title, String to, String content) {
		EmailTask emailObj = new EmailTask();
		emailObj.setId((Long)IdGen.key());
		emailObj.setEmailTitle(title);
		emailObj.setEmailTo(to);
		emailObj.setEmailContent(content);
		emailTaskService.store(emailObj);
	}
}
