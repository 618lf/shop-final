package com.tmt.common.security.filter.authz;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tmt.common.exception.ErrorCode;
import com.tmt.common.security.filter.AccessControllerFilter;
import com.tmt.common.security.subjct.Subject;
import com.tmt.common.security.utils.SecurityUtils;
import com.tmt.common.utils.WebUtils;

/**
 * 权限验证器
 * @author lifeng
 */
public abstract class AuthorizationFilter extends AccessControllerFilter{

	private String unauthorizedUrl;

	public String getUnauthorizedUrl() {
		return unauthorizedUrl;
	}

	public void setUnauthorizedUrl(String unauthorizedUrl) {
		this.unauthorizedUrl = unauthorizedUrl;
	}
	
	/**
	 * 权限验证
	 */
	protected boolean onAccessDenied(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Subject subject = SecurityUtils.getSubject();
		if (WebUtils.isAjax(request)) {
			WebUtils.sendJson(response, ErrorCode.ACCESS_DENIED.toJson());
		} else {
			if (subject.getPrincipal() == null) {
				saveRequestAndRedirectToLogin(request, response);
			} else {
				WebUtils.issueRedirect(request, response, this.getUnauthorizedUrl());
			}
		}
		return false;
	}
}