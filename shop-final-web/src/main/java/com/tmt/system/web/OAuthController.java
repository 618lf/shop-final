package com.tmt.system.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.tmt.core.config.Globals;
import com.tmt.core.security.principal.Principal;
import com.tmt.core.security.subject.Subject;
import com.tmt.core.security.utils.SecurityUtils;
import com.tmt.core.utils.StringUtils;
import com.tmt.core.utils.WebUtils;
import com.tmt.system.entity.UserAccount;
import com.tmt.system.service.UserServiceFacade;

/**
 * 授权登录 -- 不是通过用户名密码的登录方式
 * 去掉绑定
 * @author lifeng
 */
public abstract class OAuthController {
	
	@Autowired
	protected UserServiceFacade userService;
	
	/**
	 * 一般授权，只要返回码正确，就直接在网站中登录
	 * 登录之前，需要加入安全key，不然会退出系统
	 * 所有代码都是在subject 的call 中调用，
	 * 所以会自动释放线程变量
	 * @param request
	 * @param response
	 * @param principal 用户身份信息
	 * @return
	 */
	public Boolean loginShiro(HttpServletRequest request, HttpServletResponse response, Principal principal) {
		Subject subject = SecurityUtils.getSubject();
		subject.login(principal, request, response);
		return Boolean.TRUE;
	}
	
	/**
	 * 第三方登录后重定向到原始的页面
	 * @param request
	 * @param response
	 * @param fallbackUrl
	 * @return
	 */
	public String redirectToSavedRequest(String to, HttpServletRequest request, HttpServletResponse response) {
		// 有限返回这个地址
		if (StringUtils.isNotBlank(to)) {
			return WebUtils.redirectTo(to);
		}
		
		// 如果没有指定地址则返回保存的地址
		return WebUtils.redirectTo(WebUtils.getAndClearSavedRequest(request, response, Globals.staticFront));
	}
	
	/**
	 * 绑定一个现有的账户并登录
	 * @param request
	 * @param response
	 * @param account
	 */
	protected void loginAndRegister(HttpServletRequest request, HttpServletResponse response, UserAccount account) {
		this.userService.registerByAccount(account);
		this.loginShiro(request, response, new Principal(account.getUserId(), account.getId()));
		WebUtils.makerNewUser(response);
	}
	
	/**
	 * 绑定一个现有的账户并登录
	 * @param request
	 * @param response
	 * @param account
	 */
	protected void loginAndBind(HttpServletRequest request, HttpServletResponse response, UserAccount account) {
		this.userService.bindUserAccount(account);
		this.loginShiro(request, response, new Principal(account.getUserId(), account.getId()));
	}
	
	/**
	 * 直接登录
	 * @param request
	 * @param response
	 * @param account
	 */
	protected void login(HttpServletRequest request, HttpServletResponse response, UserAccount account) {
		this.loginShiro(request, response, new Principal(account.getUserId(), account.getId()));
	}
}