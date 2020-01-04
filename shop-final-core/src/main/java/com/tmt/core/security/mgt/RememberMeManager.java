package com.tmt.core.security.mgt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tmt.core.security.principal.Principal;
import com.tmt.core.security.subject.Subject;

public interface RememberMeManager {

	/**
	 * 得到记住我的身份
	 * @return
	 */
	Principal getPrincipal(HttpServletRequest request, HttpServletResponse response); 
	
	/**
     * 登录成功
     */
    void onLoginSuccess(Subject subject, HttpServletRequest request, HttpServletResponse response);
    
    /**
     * 登录失败
     */
    void onLoginFailure(HttpServletRequest request, HttpServletResponse response);
    
    /**
     * 退出登录
     */
    void onLogout(HttpServletRequest request, HttpServletResponse response);
}
