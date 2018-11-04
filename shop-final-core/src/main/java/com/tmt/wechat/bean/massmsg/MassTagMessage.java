package com.tmt.wechat.bean.massmsg;

import java.io.Serializable;

/**
 * 按标签群发的消息
 * @author lifeng
 */
public class MassTagMessage implements Serializable {

	private static final long serialVersionUID = -6625914040986749286L;
	private Long tagId;
	private String msgType;
	private String content;
	private String mediaId;
	private boolean isSendAll = false;
	private boolean sendIgnoreReprint = false;
	
	public Long getTagId() {
		return tagId;
	}
	public void setTagId(Long tagId) {
		this.tagId = tagId;
	}
	public String getMsgType() {
		return msgType;
	}
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getMediaId() {
		return mediaId;
	}
	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}
	public boolean isSendAll() {
		return isSendAll;
	}
	public void setSendAll(boolean isSendAll) {
		this.isSendAll = isSendAll;
	}
	public boolean isSendIgnoreReprint() {
		return sendIgnoreReprint;
	}
	public void setSendIgnoreReprint(boolean sendIgnoreReprint) {
		this.sendIgnoreReprint = sendIgnoreReprint;
	}
}
