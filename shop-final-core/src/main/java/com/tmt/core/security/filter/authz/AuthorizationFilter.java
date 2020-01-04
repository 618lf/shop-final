package com.tmt.core.security.filter.authz;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tmt.core.exception.ErrorCode;
import com.tmt.core.security.filter.AccessControllerFilter;
import com.tmt.core.security.subject.Subject;
import com.tmt.core.security.utils.SecurityUtils;
import com.tmt.core.utils.StringUtils;
import com.tmt.core.utils.WebUtils;

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
		if (StringUtils.contains(perm, "&") && (perms = StringUtils.split(perm, "&")) != null) {
			return subject.hasAllRoles(perms);
		} else if (StringUtils.contains(perm, "|") && (perms = StringUtils.split(perm, "|")) != null) {
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
		if (StringUtils.contains(perm, "&") && (perms = StringUtils.split(perm, "&")) != null) {
			return subject.isPermittedAll(perms);
		} else if (StringUtils.contains(perm, "|") && (perms = StringUtils.split(perm, "|")) != null) {
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