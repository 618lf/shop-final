package com.tmt.wechat.bean.massmsg;

import java.io.Serializable;

/**
 * 群发消息预览
 * @author lifeng
 */
public class MassPreviewMessage implements Serializable {
	private static final long serialVersionUID = 1L;
	private String toWxUserName;
	private String toWxUserOpenid;
	private String msgType;
	private String content;
	private String mediaId;
	
	public String getToWxUserName() {
		return toWxUserName;
	}
	public void setToWxUserName(String toWxUserName) {
		this.toWxUserName = toWxUserName;
	}
	public String getToWxUserOpenid() {
		return toWxUserOpenid;
	}
	public void setToWxUserOpenid(String toWxUserOpenid) {
		this.toWxUserOpenid = toWxUserOpenid;
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
}