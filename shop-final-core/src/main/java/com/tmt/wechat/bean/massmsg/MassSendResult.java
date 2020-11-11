package com.tmt.wechat.bean.massmsg;

import com.tmt.wechat.bean.base.BaseResult;

/**
 * 群发消息
 * 
 * @author lifeng
 */
public class MassSendResult extends BaseResult {

	private static final long serialVersionUID = 1L;

	private String msgId;
	private String msgDataId;
	
	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	public String getMsgDataId() {
		return msgDataId;
	}
	public void setMsgDataId(String msgDataId) {
		this.msgDataId = msgDataId;
	}
}
