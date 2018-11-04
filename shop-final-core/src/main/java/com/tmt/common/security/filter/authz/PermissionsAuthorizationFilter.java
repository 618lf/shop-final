package com.tmt.common.security.filter.authz;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tmt.common.security.subjct.Subject;
import com.tmt.common.security.utils.SecurityUtils;

public class PermissionsAuthorizationFilter extends AuthorizationFilter {

	/**
	 * 验证权限,必须有用户信息
	 */
	@Override
	protected boolean isAccessAllowed(HttpServletRequest request, HttpServletResponse response, Object mappedValue) throws Exception {
		Subject subject = SecurityUtils.getSubject();
		if (subject.getPrincipal() == null) {
			return false;
		}
		String[] perms = (String[]) mappedValue;
		boolean isPermitted = true;
        if (perms != null && perms.length > 0) {
            if (perms.length == 1) {
                if (!subject.isPermitted(perms[0])) {
                    isPermitted = false;
                }
            } else {
                if (!subject.isPermittedAll(perms)) {
                    isPermitted = false;
                }
            }
        }
		return isPermitted;
	}
}