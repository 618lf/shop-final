package com.tmt.wechat.bean.massmsg;

import java.io.Serializable;
import java.util.List;

import com.tmt.core.utils.Lists;

/**
 * 列表群发的消息
 * @author lifeng
 */
public class MassOpenIdMessage implements Serializable{

	private static final long serialVersionUID = -8022910911104788999L;

	private List<String> toUsers = Lists.newArrayList();
	private String msgType;
	private String content;
	private String mediaId;
	private boolean sendIgnoreReprint = false;
	
	public List<String> getToUsers() {
		return toUsers;
	}
	public void setToUsers(List<String> toUsers) {
		this.toUsers = toUsers;
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
	public boolean isSendIgnoreReprint() {
		return sendIgnoreReprint;
	}
	public void setSendIgnoreReprint(boolean sendIgnoreReprint) {
		this.sendIgnoreReprint = sendIgnoreReprint;
	}
}