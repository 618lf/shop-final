package com.tmt.common.utils;

import java.io.InputStream;
import java.util.Map;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.ClassPathResource;

/**
 * 获取bean 和注册bean
 * 
 * @author lifeng
 */
@SuppressWarnings("unchecked")
public class SpringContextHolder implements ApplicationContextAware, DisposableBean {

	protected static ApplicationContext applicationContext = null;

	/**
	 * 获得 Bean
	 * 
	 * @param name
	 * @return
	 */
	public static <T> T getBean(String name) {
		try {
			return (T) applicationContext.getBean(name);
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * 获得Bean
	 * 
	 * @param name
	 * @return
	 */
	public static <T> T getBean(Class<T> requiredType) {
		try {
			return (T) applicationContext.getBean(requiredType);
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * 指定名称和类型
	 * 
	 * @param name
	 * @param type
	 * @return
	 */
	public static <T> T getBean(String name, Class<T> type) {
		return applicationContext.getBean(name, type);
	}

	/**
	 * 得到多有的bean
	 * 
	 * @param type
	 * @return
	 */
	public static <T> Map<String, T> getBeans(Class<T> type) {
		return applicationContext.getBeansOfType(type);
	}

	/**
	 * 获取模板路径
	 * 
	 * @param template
	 * @return
	 */
	public static InputStream getTemplate(String template) {
		try {
			ClassPathResource resource = new ClassPathResource(template);
			return resource.getInputStream();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 设置引用
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		SpringContextHolder.applicationContext = applicationContext;
	}

	/**
	 * 清除引用
	 */
	@Override
	public void destroy() throws Exception {
		SpringContextHolder.applicationContext = null;
	}
}