package com.tmt.common.email;

import java.io.Serializable;

import com.tmt.common.entity.BaseEntity;

/**
 * Email 参数
 * @author root
 */
public class EmailParam implements Serializable{
	
	private static final long serialVersionUID = 6904497475245643132L;
	
	//用户监听是否改变
	public static String EMAIL_KEY = "Email";
	
	private String smtpHost; //SMTP服务器地址
	private Integer smtpPort = 25;//SMTP服务器端口
	private String smtpUsername;//SMTP用户名
	private String smtpPassword;//SMTP密码
	private Byte smtpSSLEnabled = BaseEntity.NO;//SMTP是否启用SSL
	private Byte smtpAnonymousEnabled = BaseEntity.NO;//SMTP是否匿名
	private String smtpTimeout; //发送超时时间
	public String getSmtpHost() {
		return smtpHost;
	}
	public void setSmtpHost(String smtpHost) {
		this.smtpHost = smtpHost;
	}
	public Integer getSmtpPort() {
		return smtpPort;
	}
	public void setSmtpPort(Integer smtpPort) {
		this.smtpPort = smtpPort;
	}
	public String getSmtpUsername() {
		return smtpUsername;
	}
	public void setSmtpUsername(String smtpUsername) {
		this.smtpUsername = smtpUsername;
	}
	public String getSmtpPassword() {
		return smtpPassword;
	}
	public void setSmtpPassword(String smtpPassword) {
		this.smtpPassword = smtpPassword;
	}
	public Byte getSmtpSSLEnabled() {
		return smtpSSLEnabled;
	}
	public void setSmtpSSLEnabled(Byte smtpSSLEnabled) {
		this.smtpSSLEnabled = smtpSSLEnabled;
	}
	public Byte getSmtpAnonymousEnabled() {
		return smtpAnonymousEnabled;
	}
	public void setSmtpAnonymousEnabled(Byte smtpAnonymousEnabled) {
		this.smtpAnonymousEnabled = smtpAnonymousEnabled;
	}
	public String getSmtpTimeout() {
		return smtpTimeout;
	}
	public void setSmtpTimeout(String smtpTimeout) {
		this.smtpTimeout = smtpTimeout;
	}
}