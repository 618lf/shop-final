package com.tmt.common.security.filter.authc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tmt.common.security.filter.AccessControllerFilter;
import com.tmt.common.security.utils.SecurityUtils;
import com.tmt.common.utils.WebUtils;

/**
 * 登录相关的操作
 * @author lifeng
 */
public abstract class AuthenticationFilter extends AccessControllerFilter {

	// 登录成功页面
	private String successUrl;
	
	public String getSuccessUrl() {
		return successUrl;
	}

	public void setSuccessUrl(String successUrl) {
		this.successUrl = successUrl;
	}

	/**
	 * 验证用户是否已经登录，如果没有登录（包括记住我进入系统的）
	 * 都需要再次进入系统来登录
	 * 所有请求中都不许要判断subject是否为空
	 */
	@Override
	protected boolean isAccessAllowed(HttpServletRequest request,
			HttpServletResponse response, Object mappedValue) throws Exception {
		boolean allowed = SecurityUtils.getSubject().isAuthenticated();
		if (!allowed && !isLoginSubmission(request, response)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 是 post 提交
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	protected boolean isLoginSubmission(HttpServletRequest request, HttpServletResponse	response) {
		return WebUtils.isPost(request);
	}
	
	/**
	 * 登录成功会调用这个，进入之前的页面，缺省是成功页面
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	protected void issueSuccessRedirect(HttpServletRequest request, HttpServletResponse response) throws Exception {
        WebUtils.redirectToSavedRequest(request, response, getSuccessUrl());
    }
}