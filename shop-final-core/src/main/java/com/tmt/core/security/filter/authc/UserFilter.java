package com.tmt.core.security.filter.authc;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tmt.Constants;
import com.tmt.core.codec.Encodes;
import com.tmt.core.entity.Result;
import com.tmt.core.exception.ErrorCode;
import com.tmt.core.security.filter.AccessControllerFilter;
import com.tmt.core.security.subject.Subject;
import com.tmt.core.security.utils.SecurityUtils;
import com.tmt.core.utils.StringUtils;
import com.tmt.core.utils.WebUtils;

/**
 * 用户访问权限, 有可能需要访问用户状态
 * 
 * @author lifeng
 */
public class UserFilter extends AccessControllerFilter {

	@Override
	protected boolean isAccessAllowed(HttpServletRequest request, HttpServletResponse response, Object mappedValue)
			throws Exception {
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
			if (StringUtils.isNotBlank(subject.getReason())) {
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
		if (StringUtils.isNotBlank(subject.getReason())) {
			String reason = Encodes.encodeBase64URLSafeString(subject.getReason().getBytes(Constants.DEFAULT_ENCODING));
			loginUrl = StringUtils.format("%s?reason=%s", loginUrl, reason);
		}
		WebUtils.issueRedirect(request, response, loginUrl);
	}
}