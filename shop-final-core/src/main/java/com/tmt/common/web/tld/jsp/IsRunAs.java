package com.tmt.common.web.tld.jsp;

import javax.servlet.jsp.JspException;

import com.tmt.common.security.utils.SecurityUtils;

/**
 * 是否是用户
 * 
 * @author lifeng
 */
public class IsRunAs extends SecureTag {

	private static final long serialVersionUID = 1752570911447072813L;

	@Override
	public int onDoStartTag() throws JspException {
		if (SecurityUtils.getSubject().isRunAs()) {
			return EVAL_BODY_INCLUDE;
		} else {
			return SKIP_BODY;
		}
	}
}
