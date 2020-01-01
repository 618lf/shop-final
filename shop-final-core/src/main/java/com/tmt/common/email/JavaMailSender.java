package com.tmt.common.email;

import java.util.Properties;

import com.tmt.common.entity.BaseEntity;
import com.tmt.common.utils.StringUtils;

/**
 * 邮件服务
 * @author lifeng
 */
public class JavaMailSender extends org.springframework.mail.javamail.JavaMailSenderImpl {
	
	private String from;
	
	/**
	 * 163
	 * <prop key="mail.smtp.auth">${mail.smtp.auth}</prop>  
     * <prop key="mail.smtp.timeout">${mail.smtp.timeout}</prop>  
     * 
	 * gmail
	 * <prop key="mail.smtp.auth">${mail.smtp.auth}</prop>  
     * <prop key="mail.smtp.timeout">${mail.smtp.timeout}</prop>  
     * <prop key="mail.smtp.port">465</prop>    
     * <prop key="mail.smtp.socketFactory.port">465</prop>    
     * <prop key="mail.smtp.socketFactory.fallback">false</prop>    
     * <prop key="mail.smtp.socketFactory.class">javax.net.ssl.SSLSocketFactory</prop>    
	 * @throws Exception
	 */
    public void initialize(EmailParam site) throws Exception {
		this.setUsername(site.getSmtpUsername());
		this.setFrom(site.getSmtpUsername());
		this.setHost(site.getSmtpHost());
		this.setPassword(site.getSmtpPassword());
		this.setPort(site.getSmtpPort());
		Properties javaMailProperties = this.getJavaMailProperties();
		if(BaseEntity.YES == site.getSmtpAnonymousEnabled()) {
		   javaMailProperties.setProperty("mail.smtp.auth", "false");
		} else {
		   javaMailProperties.setProperty("mail.smtp.auth", "true");
		}
		if(BaseEntity.YES == site.getSmtpSSLEnabled()) {
		   javaMailProperties.setProperty("mail.smtp.socketFactory.port", String.valueOf(site.getSmtpPort()));
		   javaMailProperties.setProperty("mail.smtp.socketFactory.fallback", "false");
		   javaMailProperties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		   javaMailProperties.remove("mail.smtp.starttls.enable");
		} else {
		   javaMailProperties.setProperty("mail.smtp.starttls.enable", "true");
		   javaMailProperties.remove("mail.smtp.starttls.enable");
		   javaMailProperties.remove("mail.smtp.socketFactory.port");
		   javaMailProperties.remove("mail.smtp.socketFactory.fallback");
		   javaMailProperties.remove("mail.smtp.socketFactory.class");
		}
		if(StringUtils.isNotBlank(site.getSmtpTimeout())) {
		   javaMailProperties.setProperty("mail.smtp.timeout", site.getSmtpTimeout());
		} else {
		   javaMailProperties.remove("mail.smtp.timeout");
		}
		this.setJavaMailProperties(javaMailProperties);
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
}