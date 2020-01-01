package com.tmt.common.security.filter.authz;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tmt.common.security.subject.Subject;
import com.tmt.common.security.utils.SecurityUtils;

/**
 * 角色验证
 * 
 * @author lifeng
 */
public class RolesAuthorizationFilter extends AuthorizationFilter {

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
				isPermitted = this.hasRoles(subject, perm);
				if (isPermitted) {
					break;
				}
			}
		}
		return isPermitted;
	}
}
