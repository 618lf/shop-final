package com.sample.sso;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sample.sso.domain.User;
import com.tmt.core.security.subject.Subject;
import com.tmt.core.security.utils.SecurityUtils;

/**
 * 
 * 外部的SSO登录操作
 * 
 * @author lifeng
 */
public class ExternalSsoFilter implements Filter {

	private String USER_SESSION_KEY = "USER";
	private String logOffCallBackUrl = "/admin/logout";
	private String param_token = "token";

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		String requestUri = request.getRequestURI();
		String requestUrl = request.getRequestURL().toString();
		if (request.getQueryString() != null) {
			requestUrl = requestUrl + "?" + request.getQueryString();
		}

		System.out.println("当前请求地址：" + requestUrl);

		/**
		 * 退出登录
		 */
		if (requestUri != null && requestUri.equals(logOffCallBackUrl)) {
			this.logout(request, response);
		}

		/**
		 * 模拟登录
		 */
		else if (request.getParameter(param_token) != null && !this.isUser(request)) {

			/**
			 * 执行登录
			 */
			this.login(request, response);

			/**
			 * 后续处理
			 */
			chain.doFilter(request, response);
		}

		/**
		 * 跳转到登录
		 */
		else if (!this.isUser(request)) {
			String jumptourl = requestUrl.indexOf("?") != -1 ? requestUrl + "&token=111" : requestUrl + "?token=111";
			response.sendRedirect(jumptourl);
		}

		/**
		 * 已登录的用户
		 */
		else {
			/**
			 * 后续处理
			 */
			chain.doFilter(request, response);
		}
	}

	/**
	 * 是否登录
	 * 
	 * @return
	 */
	private boolean isUser(HttpServletRequest request) {
		return request.getSession(false) != null && request.getSession(false).getAttribute(USER_SESSION_KEY) != null;
	}

	/**
	 * 模拟退出登录
	 * 
	 * @param request
	 * @param response
	 */
	private void logout(HttpServletRequest request, HttpServletResponse response) {
		Subject subject = SecurityUtils.getSubject();
		if (subject.getPrincipal() != null) {
			subject.logout(request, response);
		}
	}

	/**
	 * 模拟登录
	 * 
	 * @param request
	 * @param response
	 */
	private void login(HttpServletRequest request, HttpServletResponse response) {

		/**
		 * 创建Session -- 这部分已经在外部框架中执行过了
		 */
		User user = new User();
		user.personId = "123";
		user.personName = "中国海关//测试用户";
		request.getSession(true).setAttribute(USER_SESSION_KEY, user);

		/**
		 * 执行登陆操作
		 */
		Subject subject = SecurityUtils.getSubject();
		subject.login(request, response);
	}
}
