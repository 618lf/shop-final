package com.tmt.core.security.filter.authz;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tmt.core.security.subject.Subject;
import com.tmt.core.security.utils.SecurityUtils;

public class PermissionsAuthorizationFilter extends AuthorizationFilter {

	/**
	 * 验证权限,必须有用户信息
	 */
	@Override
	protected boolean isAccessAllowed(HttpServletRequest request, HttpServletResponse response, Object mappedValue)
			throws Exception {
		Subject subject = SecurityUtils.getSubject();
		if (subject.getPrincipal() == null) {
			return false;
		}
		String[] perms = (String[]) mappedValue;
		if (perms == null || perms.length == 0) {
			return true;
		}
		boolean isPermitted = true;
		if (perms != null && perms.length > 0) {
			for (String perm : perms) {
				isPermitted = this.isPermitted(subject, perm);
				if (isPermitted) {
					break;
				}
			}
		}
		return isPermitted;
	}
}