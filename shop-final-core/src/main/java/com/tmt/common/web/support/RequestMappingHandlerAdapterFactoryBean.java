package com.tmt.common.web.support;

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

/**
 * 获取系统初始化的 RequestMappingHandlerAdapter
 * @author lifeng
 */
public class RequestMappingHandlerAdapterFactoryBean implements FactoryBean<RequestMappingHandlerAdapter>, ApplicationContextAware, InitializingBean{

	public static final String HANDLER_ADAPTER_BEAN_NAME = RequestMappingHandlerAdapter.class.getName();
	private RequestMappingHandlerAdapter handlerAdapter;
	private ApplicationContext applicationContext;
	
	@Override
	public RequestMappingHandlerAdapter getObject() throws Exception {
		return handlerAdapter;
	}

	@Override
	public Class<?> getObjectType() {
		return RequestMappingHandlerAdapter.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		Map<String, HandlerAdapter> matchingBeans =
				BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, HandlerAdapter.class, true, false);
		if (!matchingBeans.isEmpty()) {
			handlerAdapter = (RequestMappingHandlerAdapter) matchingBeans.get(HANDLER_ADAPTER_BEAN_NAME);
		}
		// for gc
		applicationContext = null;
	}
}
