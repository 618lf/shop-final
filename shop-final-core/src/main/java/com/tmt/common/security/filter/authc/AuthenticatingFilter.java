package com.tmt.common.security.filter.authc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tmt.common.config.Globals;
import com.tmt.common.entity.Result;
import com.tmt.common.exception.ErrorCode;
import com.tmt.common.security.context.AuthenticationToken;
import com.tmt.common.security.exception.AuthenticationException;
import com.tmt.common.security.subjct.Subject;
import com.tmt.common.security.utils.SecurityUtils;
import com.tmt.common.security.utils.StringUtils;
import com.tmt.common.utils.WebUtils;

/**
 * 实际的登录操作
 * 
 * @author lifeng
 */
public class AuthenticatingFilter extends AuthenticationFilter {

	/**
	 * 进来的是登录的地址
	 */
	@Override
	protected boolean onAccessDenied(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		// 已认证不用再处理
		boolean authenticated = SecurityUtils.getSubject().isAuthenticated();
		if (authenticated) {
			issueSuccessRedirect(request, response);
			return false;
		}
		
		// 如果是登录请求 post 提交的
		if (isLoginSubmission(request, response)) {
			return executeLogin(request, response);
		}
		WebUtils.sendJson(response, Result.error(ErrorCode.OPERATE_FAILURE).toJson());
		return false;
	}

	/**
	 * 登录逻辑
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected boolean executeLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			Subject subject = SecurityUtils.getSubject();
			subject.login(request, response);
			return onLoginSuccess(subject, request, response);
		} catch (AuthenticationException e) {
			return onLoginFailure(e, request, response);
		}
	}

	/**
	 * 登录成功则不需要继续执行
	 * 
	 * @param token
	 * @param subject
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected boolean onLoginSuccess(Subject subject, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		// 支持ajax 登录,会输入操作成功
		if (WebUtils.isAjax(request)) {
			WebUtils.sendJson(response, Result.success().toJson());
			return false;
		} 
		
		// 非ajax 登录在会调转页面
		issueSuccessRedirect(request, response);
		return false;
	}

	/**
	 * 登录失败则继续执行 -- 一定会抛出异常
	 * Ajax 请求不会往下执行，请不要在 login post 控制器中重要的逻辑
	 * @param e
	 * @param request
	 * @param response
	 * @return
	 */
	protected boolean onLoginFailure(AuthenticationException e, HttpServletRequest request, HttpServletResponse response) {
		ErrorCode code = e.getErrorCode();
		
		// ajax 请求会直接输出异常信息
		if (WebUtils.isAjax(request)) {
			WebUtils.sendJson(response, Result.error(code).toJson());
			return false;
		} 
		
		// 正常请求
		request.setAttribute(Globals.REQUEST_ERROR_CODE_PARAM, code);
		return true;
	}

	/**
	 * 登录到配置的页面
	 */
	@Override
	protected void issueSuccessRedirect(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (WebUtils.isAjax(request)) {
			WebUtils.sendJson(response, Result.error(ErrorCode.OPERATE_SECCESS).toJson());
		} else {
			WebUtils.issueRedirect(request, response, this.getReturnUrl(request));
		}
	}
	
	/**
	 * 登录成功之后会返回这个地址
	 * @return
	 */
	protected String getReturnUrl(HttpServletRequest request) {
		String returnUrl = WebUtils.getCleanParam(request, AuthenticationToken.returnUrl);
		return StringUtils.hasText(returnUrl) ? returnUrl: this.getSuccessUrl();
	}
}