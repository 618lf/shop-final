package com.tmt.core.web.security.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 方法级别的拦截器
 * 
 * @author lifeng
 */
public abstract class MethodInterceptor extends HandlerInterceptorAdapter {

	/**
	 * 只支持 HandlerMethod 的拦截
	 */
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		if (handler instanceof HandlerMethod) {
			return doHandle(request, response, (HandlerMethod) handler);
		}
		return true;
	}

	/**
	 * 执行处理
	 * 
	 * @param request  请求
	 * @param response 响应
	 * @param handler  HandlerMethod
	 * @return 是否继续处理
	 */
	protected abstract boolean doHandle(HttpServletRequest request, HttpServletResponse response,
			HandlerMethod handler);
}
