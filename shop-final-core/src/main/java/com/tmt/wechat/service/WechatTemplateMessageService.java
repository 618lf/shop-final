package com.tmt.wechat.service;

import com.tmt.wechat.bean.template.TemplateMessageResult;

public interface WechatTemplateMessageService {

	/**
	 * 发送模板消息
	 * @param messageJson
	 * @return
	 */
	public TemplateMessageResult sendTemplateMessage(String messageJson);
}