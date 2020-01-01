package com.shop.config.email;

import static com.shop.Application.APP_LOGGER;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.tmt.Constants;
import com.tmt.common.email.JavaMailSender;
import com.tmt.common.email.MimeMailService;

/**
 * 邮箱配置
 * 
 * @author lifeng
 */
@Configuration
@ConditionalOnProperty(prefix = "spring.application", name = "enableEmail", matchIfMissing = true)
public class EmailAutoConfiguration {

	public EmailAutoConfiguration() {
		APP_LOGGER.debug("Loading Email");
	}

	/**
	 * 创建一个邮件发送器
	 * 
	 * @return
	 */
	@Bean
	public JavaMailSender mailSender() {
		JavaMailSender mailSender = new JavaMailSender();
		mailSender.setDefaultEncoding(Constants.DEFAULT_ENCODING.toString());
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