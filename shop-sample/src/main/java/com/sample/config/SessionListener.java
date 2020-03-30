package com.sample.config;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionListener implements HttpSessionListener, HttpSessionAttributeListener, ServletContextListener {

	@Override
	public void attributeAdded(HttpSessionBindingEvent se) {
	}

	@Override
	public void attributeRemoved(HttpSessionBindingEvent se) {
	}

	@Override
	public void attributeReplaced(HttpSessionBindingEvent se) {
	}

	@Override
	public void sessionCreated(HttpSessionEvent se) {
		System.out.println("Session 创建: " + se.getSession().getId());
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		System.out.println("Session 销毁: " + se.getSession().getId());
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		System.out.println("系统启动");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		System.out.println("系统停止");
	}
}
