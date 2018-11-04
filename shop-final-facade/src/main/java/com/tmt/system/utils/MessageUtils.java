package com.tmt.system.utils;

import com.tmt.common.config.Globals;
import com.tmt.common.utils.SpringContextHolder;
import com.tmt.common.utils.StringUtil3;
import com.tmt.system.entity.Message;
import com.tmt.system.service.MessageServiceFacade;

/**
 * 站内消息
 * @author lifeng
 */
public class MessageUtils {

	private static MessageServiceFacade messageService = SpringContextHolder.getBean(MessageServiceFacade.class);
	
	/**
	 * 发送错误通知
	 * @param content
	 */
	public static void sendErrorMessage(String content) {
		Message template = new Message();
		template.setReceiverUserId(Globals.ROOT);
		template.setReceiverUserName("系统管理员");
		template.setContent(StringUtil3.abbr(content, 1000));
		template.setTitle("系统错误");
		template.setSendUserId(null);
		template.setSendUserName("系统错误");
		messageService.send(template);
	}
}