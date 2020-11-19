package com.tmt.shop.entity;

import java.io.Serializable;

import com.tmt.core.entity.BaseEntity;

public class NoticeSetting extends BaseEntity<Byte> implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name; // 通知名称
	private Byte templateMsg; // 是否发送模板消息
	private String tmTemplate; // 模板消息模板
	private Byte siteMsg; // 是否发送站内消息
	private String smTemplate; // 模板消息模板
	private Byte smsMsg; // 是否发送站内消息
	private String smsTemplate; // 模板消息模板

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTmTemplate() {
		return tmTemplate;
	}

	public void setTmTemplate(String tmTemplate) {
		this.tmTemplate = tmTemplate;
	}

	public Byte getTemplateMsg() {
		return templateMsg;
	}

	public void setTemplateMsg(Byte templateMsg) {
		this.templateMsg = templateMsg;
	}

	public Byte getSiteMsg() {
		return siteMsg;
	}

	public void setSiteMsg(Byte siteMsg) {
		this.siteMsg = siteMsg;
	}

	public Byte getSmsMsg() {
		return smsMsg;
	}

	public void setSmsMsg(Byte smsMsg) {
		this.smsMsg = smsMsg;
	}

	public String getSmTemplate() {
		return smTemplate;
	}

	public void setSmTemplate(String smTemplate) {
		this.smTemplate = smTemplate;
	}

	public String getSmsTemplate() {
		return smsTemplate;
	}

	public void setSmsTemplate(String smsTemplate) {
		this.smsTemplate = smsTemplate;
	}

	@Override
	public Byte prePersist() {
		return this.id;
	}
}
