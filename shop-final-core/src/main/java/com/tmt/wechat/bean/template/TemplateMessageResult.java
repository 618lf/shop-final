package com.tmt.wechat.bean.template;

import com.tmt.wechat.bean.base.BaseResult;

public class TemplateMessageResult extends BaseResult{

	private static final long serialVersionUID = -6167967117347453757L;
	
	private Long msgid;

	public Long getMsgid() {
		return msgid;
	}

	public void setMsgid(Long msgid) {
		this.msgid = msgid;
	}
}