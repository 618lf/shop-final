package com.shop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.ConfigurableApplicationContext;

import com.tmt.OS;
import com.tmt.common.utils.StringUtil3;

/**
 * 
 * Spring 启动
 * 
 * @author lifeng
 */
public class Application extends SpringApplication {

	/**
	 * 系统启动的日志
	 */
	public final static Logger APP_LOGGER = LoggerFactory.getLogger(Application.class);

	/**
	 * 全局的 context
	 */
	public static ConfigurableApplicationContext CONTEXT;

	/**
	 * 初始化
	 */
	public Application(Class<?>... primarySources) {
		super(primarySources);
	}

	/**
	 * 启动服务
	 * 
	 * @param primarySource
	 * @param args
	 * @return
	 */
	public static ConfigurableApplicationContext run(Class<?> primarySource, String... args) {
		long start = System.currentTimeMillis();
		Application application = new Application(primarySource);
		application.setBannerMode(Banner.Mode.OFF);
		CONTEXT = (ConfigurableApplicationContext) application.run(args);
		long end = System.currentTimeMillis();
		APP_LOGGER.debug("Server start success in " + (end - start) / 1000 + "s, Listen on [" + getAddresses() + "]");
		return CONTEXT;
	}

	/**
	 * 服务地址
	 * 
	 * @return
	 */
	public static String getAddresses() {
		ServerProperties properties = CONTEXT.getBean(ServerProperties.class);
		StringBuilder address = new StringBuilder();
		if (properties.getSsl() != null) {
			address.append("https");
		} else {
			address.append("http");
		}
		address.append("://").append("%s");
		int port = properties.getPort();
		if (port != 80) {
			address.append(":").append(port);
		}
		String hostName = null;
		if (properties.getAddress() == null || !"127.0.0.1".equals(properties.getAddress().getHostAddress())) {
			hostName = OS.ip();
		}
		return StringUtil3.format(address.toString(), hostName);
	}

	/**
	 * 停止服务
	 */
	public static void stop() {
		if (CONTEXT != null) {
			exit(CONTEXT, new ExitCodeGenerator[] {});
			CONTEXT = null;
		}
	}
}