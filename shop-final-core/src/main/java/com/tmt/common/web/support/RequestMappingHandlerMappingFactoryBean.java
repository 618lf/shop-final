package com.tmt.common.web.support;

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * 获取系统初始化的 RequestMappingHandlerMapping
 * @author lifeng
 */
public class RequestMappingHandlerMappingFactoryBean implements FactoryBean<RequestMappingHandlerMapping>, ApplicationContextAware, InitializingBean{

	public static final String HANDLER_MAPPING_BEAN_NAME = RequestMappingHandlerMapping.class.getName();
	
	private RequestMappingHandlerMapping handlerMapping;
	private ApplicationContext applicationContext;
	
	@Override
	public RequestMappingHandlerMapping getObject() throws Exception {
		return handlerMapping;
	}

	@Override
	public Class<?> getObjectType() {
		return RequestMappingHandlerMapping.class;
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
		Map<String, HandlerMapping> matchingBeans =
				BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, HandlerMapping.class, true, false);
		if (!matchingBeans.isEmpty()) {
			handlerMapping = (RequestMappingHandlerMapping) matchingBeans.get(HANDLER_MAPPING_BEAN_NAME);
		}
		
		// for gc
		applicationContext = null;
	}
}
