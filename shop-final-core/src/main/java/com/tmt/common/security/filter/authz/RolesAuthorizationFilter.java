package com.tmt.common.security.filter.authz;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tmt.common.security.subjct.Subject;
import com.tmt.common.security.utils.SecurityUtils;

/**
 * 角色验证
 * @author lifeng
 */
public class RolesAuthorizationFilter extends AuthorizationFilter {

	@Override
	protected boolean isAccessAllowed(HttpServletRequest request,
			HttpServletResponse response, Object mappedValue) throws Exception {
		Subject subject = SecurityUtils.getSubject();
		if (subject.getPrincipal() == null) {
			return false;
		}
		
		String[] rolesArray = (String[]) mappedValue;
		if (rolesArray == null || rolesArray.length == 0) {
            return true;
        }
		return subject.hasAllRoles(rolesArray);
	}
}
