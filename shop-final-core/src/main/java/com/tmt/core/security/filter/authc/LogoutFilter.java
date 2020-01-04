package com.tmt.core.security.filter.authc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tmt.core.exception.ErrorCode;
import com.tmt.core.security.filter.AdviceFilter;
import com.tmt.core.security.subject.Subject;
import com.tmt.core.security.utils.SecurityUtils;
import com.tmt.core.utils.WebUtils;

/**
 * 退出登录
 * @author lifeng
 *
 */
public class LogoutFilter extends AdviceFilter{

	private String redirectUrl;

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	@Override
	protected boolean preHandle(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Subject subject = SecurityUtils.getSubject();
		if (subject.getPrincipal() != null) {
			subject.logout(request, response);
		}
		if (!WebUtils.isAjax(request)) {
			WebUtils.issueRedirect(request, response, redirectUrl);
		} else {
			WebUtils.sendJson(response, ErrorCode.OPERATE_SECCESS.toJson());
		}
		return false;
	}
}