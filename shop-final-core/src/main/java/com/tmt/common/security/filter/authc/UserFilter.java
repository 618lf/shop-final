package com.tmt.common.security.filter.authc;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tmt.common.config.Globals;
import com.tmt.common.entity.Result;
import com.tmt.common.exception.ErrorCode;
import com.tmt.common.security.filter.AccessControllerFilter;
import com.tmt.common.security.subjct.Subject;
import com.tmt.common.security.utils.SecurityUtils;
import com.tmt.common.utils.Encodes;
import com.tmt.common.utils.StringUtil3;
import com.tmt.common.utils.WebUtils;

/**
 * 用户访问权限, 有可能需要访问用户状态
 * @author lifeng
 */
public class UserFilter extends AccessControllerFilter {

	@Override
	protected boolean isAccessAllowed(HttpServletRequest request,
			HttpServletResponse response, Object mappedValue) throws Exception {
		Subject subject = SecurityUtils.getSubject();
		return subject.getPrincipal() != null;
	}

	/**
	 * ajax 才能显示退出原因
	 */
	@Override
	protected boolean onAccessDenied(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Subject subject = SecurityUtils.getSubject();
		if (WebUtils.isAjax(request)) {
			ErrorCode code = ErrorCode.NO_USER;
			if (StringUtil3.isNotBlank(subject.getReason())) {
				code = code.clone();
				code.setReason(subject.getReason());
			}
			WebUtils.sendJson(response, Result.error(code).toJson());
		} else {
			this.saveRequestAndRedirectToLogin(request, response);
		}
		return false;
	}

	/**
	 * 子类可以覆盖此方法
	 */
	@Override
	protected void saveRequestAndRedirectToLogin(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		Subject subject = SecurityUtils.getSubject();
		String loginUrl = getLoginUrl();
		if (StringUtil3.isNotBlank(subject.getReason())) {
			String reason = Encodes.encodeUrlSafeBase64(subject.getReason().getBytes(Globals.DEFAULT_ENCODING));
			loginUrl = StringUtil3.format("%s?reason=%s", loginUrl, reason);
		}
		WebUtils.issueRedirect(request, response, loginUrl);
	}
}