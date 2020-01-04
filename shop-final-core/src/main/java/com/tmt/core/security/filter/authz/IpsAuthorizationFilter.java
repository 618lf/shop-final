package com.tmt.core.security.filter.authz;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tmt.core.security.subject.Subject;
import com.tmt.core.security.utils.SecurityUtils;
import com.tmt.core.utils.WebUtils;

/**
 * 对 IP 的验证
 * 
 * @author lifeng
 */
public class IpsAuthorizationFilter extends AuthorizationFilter {

	@Override
	protected boolean isAccessAllowed(HttpServletRequest request, HttpServletResponse response, Object mappedValue)
			throws Exception {
		Subject subject = SecurityUtils.getSubject();
		if (subject.getPrincipal() == null) {
			return false;
		}
		String[] perms = (String[]) mappedValue;
		boolean isPermitted = true;
		if (perms != null && perms.length > 0) {
			String ip = WebUtils.getRemoteAddr(request);
			return this.isPermitted(ip, perms);
		}
		return isPermitted;
	}

	private boolean isPermitted(String ip, String[] perms) {
		for (String perm : perms) {
			if (perm != null && ip != null && perm.equals(ip)) {
				return true;
			}
		}
		return false;
	}
}