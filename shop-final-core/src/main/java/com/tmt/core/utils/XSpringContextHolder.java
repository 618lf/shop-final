package com.tmt.core.utils; 

import javax.servlet.ServletContext;

import org.springframework.web.context.ServletContextAware;

import com.tmt.core.utils.SpringContextHolder;

/**
 * 以静态变量保存Spring ApplicationContext, 可在任何代码任何地方任何时候取出ApplicaitonContext.
 * 
 * @author TMT
 * @date 2013-5-29 下午1:25:40
 */
public class XSpringContextHolder extends SpringContextHolder implements ServletContextAware {

	private static ServletContext servletContext = null;
	
	/**
	 * 获取系统上下文
	 * @return
	 */
	public static ServletContext getServletContext() {
		return servletContext;
	}
	
	/**
	 * 获取servletContext系统全局上下文
	 */
	@Override
	public void setServletContext(ServletContext servletContext) {
		XSpringContextHolder.servletContext = servletContext;
	}
}