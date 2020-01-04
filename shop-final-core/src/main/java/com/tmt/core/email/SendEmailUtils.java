package com.tmt.core.email;

import java.io.File;

import com.tmt.core.codec.Digests;
import com.tmt.core.utils.CacheUtils;
import com.tmt.core.utils.JsonMapper;
import com.tmt.core.utils.SpringContextHolder;
import com.tmt.core.utils.StringUtils;

/**
 * 邮件发送工具类
 * 请使用这个类来发放邮件
 * @author lifeng
 */
public class SendEmailUtils {

	//MD5
	private static volatile String md5Code;
	
	/**
	 * 校验是否改变
	 * @return
	 */
	private synchronized static void md5Check() {
		EmailParam emailParam = CacheUtils.get(EmailParam.EMAIL_KEY);
		String md5 = Digests.md5(JsonMapper.toJson(emailParam));
		if (emailParam != null && StringUtils.isNotBlank(md5) && !md5.equals(md5Code)) {
		    md5Code = md5;
		    try {
			  JavaMailSender sender = SpringContextHolder.getBean(JavaMailSender.class);
			  sender.initialize(emailParam);
		    } catch (Exception e) {}
		}
	}
	
	/**
	 * 邮件服务类
	 */
	private static MimeMailService mimeMailService = SpringContextHolder.getBean(MimeMailService.class);
	/**
	 * 发送执行的内容的邮件
	 * @param toArray
	 * @param CcArray
	 * @param context
	 * @return
	 */
	public static Boolean sendNotificationMail(String subject, String[] toArray , String[]  ccArray, String content ) {
		md5Check();
		return mimeMailService.sendNotificationMail(subject, toArray, ccArray, content);
	}
	
	/**
	 * 发送MIME格式的用户修改通知邮件. 并带附件  基础
	 */
	public Boolean sendNotificationMail(String subject, String[] toArray , String[]  ccArray ,String content, File[] attachments ) {
		md5Check();
		return mimeMailService.sendNotificationMail(subject, toArray, ccArray, content, attachments);
	}
}