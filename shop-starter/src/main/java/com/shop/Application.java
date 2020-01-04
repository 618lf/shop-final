package com.shop;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.AnnotationUtils;

import com.tmt.OS;
import com.tmt.core.utils.Sets;
import com.tmt.core.utils.StringUtils;

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
	private static Application ME;
	private static ConfigurableApplicationContext _CONTEXT;

	/**
	 * 初始化
	 */
	public Application(Class<?>... primarySources) {

		// 默认的扫描
		super(primarySources);

		// 指向唯一
		ME = this;
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
		_CONTEXT = (ConfigurableApplicationContext) application.run(args);
		long end = System.currentTimeMillis();
		APP_LOGGER.debug("Server start success in " + (end - start) / 1000 + "s, Listen on [" + getAddresses() + "]");
		return _CONTEXT;
	}

	/**
	 * 服务地址
	 * 
	 * @return
	 */
	public static String getAddresses() {
		ServerProperties properties = _CONTEXT.getBean(ServerProperties.class);
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
		return StringUtils.format(address.toString(), hostName);
	}

	/**
	 * 停止服务
	 */
	public static void stop() {
		if (_CONTEXT != null) {
			exit(_CONTEXT, new ExitCodeGenerator[] {});
			_CONTEXT = null;
			ME = null;
		}
	}

	/**
	 * 当前应用
	 * 
	 * @return
	 */
	public static Application me() {
		return ME;
	}

	/**
	 * 返回扫描的包
	 * 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static Set<String> getScanPackages() {
		Set<String> packages = Sets.newHashSet();
		Set<Object> sources = Application.me().getAllSources();
		if (sources != null && !sources.isEmpty()) {
			for (Object source : sources) {
				if (source instanceof Class<?>) {
					Class<?> sourceClass = ((Class<?>) source);
					Set<ComponentScan> scans = AnnotationUtils.getRepeatableAnnotations(sourceClass,
							ComponentScan.class);
					for (ComponentScan scan : scans) {
						packages.addAll(parseComponentScan(sourceClass, scan));
					}
				}
			}
		}
		return packages;
	}

	/**
	 * 简单的处理扫描的包
	 * 
	 * @param sourceClass
	 * @param scan
	 * @return
	 */
	static Set<String> parseComponentScan(Class<?> sourceClass, ComponentScan scan) {
		String[] packages = scan.basePackages();
		if (packages == null || packages.length == 0) {
			return Sets.newHashSet(sourceClass.getPackage().getName());
		}
		return Sets.newHashSet(packages);
	}
}