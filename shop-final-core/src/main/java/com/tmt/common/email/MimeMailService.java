package com.tmt.common.email;

import java.io.File;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.MimeMessageHelper;

import com.tmt.Constants;

/**
 * MIME邮件服务类.
 * 
 * 由Freemarker引擎生成的的html格式邮件, 并带有附件.
 * 
 * @author LIFNG
 */
public class MimeMailService {

	private JavaMailSender mailSender;
	
	/**
	 * 发送MIME格式的用户修改通知邮件. 不带附件
	 * toArray : 发送
	 * CcArray : 抄送
	 */
	public Boolean sendNotificationMail(String subject, String[] toArray, String[]  ccArray, String content) {
		return this.sendNotificationMail(subject,toArray, ccArray, content, new File[0]);
	}
	
	/**
	 * 发送MIME格式的用户修改通知邮件. 并带附件  基础
	 */
	public Boolean sendNotificationMail(String subject, String[] toArray , String[]  ccArray ,String content, File[] attachments ) {
		MimeMessage msg = this.generateMimeMessage(subject, toArray, ccArray, content, attachments);
		return this.sendNotificationMail(msg);
	}
	
	/**
	 * 发送MIME格式的用户修改通知邮件. 自定义格式
	 */
	public Boolean sendNotificationMail(MimeMessage msg) {
		try {
			mailSender.send(msg);
			return Boolean.TRUE;
		}catch (Exception e) {
			return Boolean.FALSE;
		}
	}
	private MimeMessage generateMimeMessage(String subject, String[] toArray, String[] ccArray, String content, File[] attachments) {
		try {
			MimeMessage msg = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(msg, true, Constants.DEFAULT_ENCODING.toString());
			helper.setFrom(mailSender.getFrom());
			helper.setTo(toArray);
			helper.setCc(ccArray == null?new String[0]:ccArray);
			helper.setSubject(subject);
			helper.setText(content, true);
			//添加附件
			if( msg != null && attachments != null && attachments.length != 0 ) {
				for( File attachment : attachments ) {
					helper.addAttachment(attachment.getName(), attachment);
			    }
			}
			return msg;
		} catch (Exception e) {
			return null;
		} 
	}
	
	/**
	 * Spring的MailSender.
	 */
	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}
}