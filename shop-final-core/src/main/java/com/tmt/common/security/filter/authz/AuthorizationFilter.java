package com.tmt.common.security.filter.authz;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tmt.common.exception.ErrorCode;
import com.tmt.common.security.filter.AccessControllerFilter;
import com.tmt.common.security.subjct.Subject;
import com.tmt.common.security.utils.SecurityUtils;
import com.tmt.common.utils.StringUtil3;
import com.tmt.common.utils.WebUtils;

/**
 * 权限验证器
 * 
 * @author lifeng
 */
public abstract class AuthorizationFilter extends AccessControllerFilter {

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

	/**
	 * 权限的判断
	 * 
	 * @param subject
	 * @param perm
	 * @return
	 */
	protected boolean hasRoles(Subject subject, String perm) {
		String[] perms = null;
		if (StringUtil3.contains(perm, "&") && (perms = StringUtil3.split(perm, "&")) != null) {
			return subject.hasAllRoles(perms);
		} else if (StringUtil3.contains(perm, "|") && (perms = StringUtil3.split(perm, "|")) != null) {
			boolean[] roles = subject.hasRoles(perms);
			for (boolean r : roles) {
				if (r) {
					return r;
				}
			}
			return false;
		} else {
			return subject.hasRole(perm);
		}
	}

	/**
	 * 权限的判断
	 * 
	 * @param subject
	 * @param perm
	 * @return
	 */
	protected boolean isPermitted(Subject subject, String perm) {
		String[] perms = null;
		if (StringUtil3.contains(perm, "&") && (perms = StringUtil3.split(perm, "&")) != null) {
			return subject.isPermittedAll(perms);
		} else if (StringUtil3.contains(perm, "|") && (perms = StringUtil3.split(perm, "|")) != null) {
			boolean[] roles = subject.isPermitted(perms);
			for (boolean r : roles) {
				if (r) {
					return r;
				}
			}
			return false;
		} else {
			return subject.isPermitted(perm);
		}
	}
}