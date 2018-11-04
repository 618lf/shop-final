package com.tmt.common.security.filter;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tmt.common.utils.WebUtils;

/**
 * 访问控制
 * @author lifeng
 */
public abstract class AccessControllerFilter extends PathMatchingFilter {

	private String loginUrl;

	public String getLoginUrl() {
		return loginUrl;
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	/**
	 * 是否有权访问这个请求
	 * @param request
	 * @param response
	 * @param mappedValue
	 * @return
	 * @throws Exception
	 */
	protected abstract boolean isAccessAllowed(HttpServletRequest request, HttpServletResponse response, Object mappedValue) throws Exception;

	/**
	 * 如果没权访问这个请求，需要做什么动作
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected abstract boolean onAccessDenied(HttpServletRequest request, HttpServletResponse response) throws Exception;

	/**
	 * 将 preHandle 分为两步来执行
	 */
	public boolean onPreHandle(HttpServletRequest request, HttpServletResponse response, Object mappedValue) throws Exception {
		return isAccessAllowed(request, response, mappedValue) || onAccessDenied(request, response);
	}

	/**
	 * 是否时登录的请求
	 * @param request
	 * @param response
	 * @return
	 */
	protected boolean isLoginRequest(HttpServletRequest request, HttpServletResponse response) {
		return pathsMatch(getLoginUrl(), request);
	}

	/**
	 * 保存当前请求并重定向到登录页面
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	protected void saveRequestAndRedirectToLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
		redirectToLogin(request, response);
	}

	/**
	 * 重定向到登录页面
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	protected void redirectToLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String loginUrl = getLoginUrl();
		WebUtils.issueRedirect(request, response, loginUrl);
	}
}