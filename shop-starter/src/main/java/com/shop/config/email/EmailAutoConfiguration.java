package com.shop.config.email;

import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import com.tmt.common.config.Globals;
import com.tmt.common.email.JavaMailSender;
import com.tmt.common.email.MimeMailService;

/**
 * 邮箱配置
 * 
 * @author lifeng
 */
@Configuration
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE + 10)
@Order(Ordered.HIGHEST_PRECEDENCE + 10)
public class EmailAutoConfiguration {

	/**
	 * 创建一个邮件发送器
	 * @return
	 */
	@Bean
	public JavaMailSender mailSender() {
		JavaMailSender mailSender = new JavaMailSender();
		mailSender.setDefaultEncoding(Globals.DEFAULT_ENCODING);
		return mailSender;
	}
	
	/**
	 * 邮件服务
	 * 
	 * @param mailSender
	 * @return
	 */
	@Bean
	public MimeMailService mimeMailService(JavaMailSender mailSender) {
		MimeMailService mimeMailService = new MimeMailService();
		mimeMailService.setMailSender(mailSender);
		return mimeMailService;
	}
}