package com.tmt.system.entity;

import java.io.Serializable;

import com.tmt.core.entity.BaseEntity;

/**
 * 邮件任务
 * @author lifeng
 */
public class EmailTask extends BaseEntity<Long> implements Serializable{
   
	private static final long serialVersionUID = 1L;
	
    private String emailForm;
    private String emailTitle;
    private String emailTo;
    private String emailCc;
    private String emailTemplate;
    private String emailContent;
    private Byte sendFlag;//0发送失败,1发送成功
    private Byte status;//0待发送,
    
	public String getEmailForm() {
		return emailForm;
	}
	public void setEmailForm(String emailForm) {
		this.emailForm = emailForm;
	}
	public String getEmailTitle() {
		return emailTitle;
	}
	public void setEmailTitle(String emailTitle) {
		this.emailTitle = emailTitle;
	}
	public String getEmailTo() {
		return emailTo;
	}
	public void setEmailTo(String emailTo) {
		this.emailTo = emailTo;
	}
	public String getEmailCc() {
		return emailCc;
	}
	public void setEmailCc(String emailCc) {
		this.emailCc = emailCc;
	}
	public String getEmailTemplate() {
		return emailTemplate;
	}
	public void setEmailTemplate(String emailTemplate) {
		this.emailTemplate = emailTemplate;
	}
	public String getEmailContent() {
		return emailContent;
	}
	public void setEmailContent(String emailContent) {
		this.emailContent = emailContent;
	}
	public Byte getSendFlag() {
		return sendFlag;
	}
	public void setSendFlag(Byte sendFlag) {
		this.sendFlag = sendFlag;
	}
	public Byte getStatus() {
		return status;
	}
	public void setStatus(Byte status) {
		this.status = status;
	}
}