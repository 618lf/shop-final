package com.tmt.common.web.tld.jsp;

import javax.servlet.jsp.JspException;

import com.tmt.common.security.utils.SecurityUtils;

/**
 * 是否是用户
 * 
 * @author lifeng
 */
public class IsUser extends SecureTag {

	private static final long serialVersionUID = 1752570911447072813L;

	@Override
	public int onDoStartTag() throws JspException {
		if (SecurityUtils.getSubject() != null && SecurityUtils.getSubject().getPrincipal() != null) {
			if (logger.isTraceEnabled()) {
				logger.trace("Subject has known identity (aka 'principal').  " + "Tag body will be evaluated.");
			}
			return EVAL_BODY_INCLUDE;
		} else {
			if (logger.isTraceEnabled()) {
				logger.trace("Subject does not exist or have a known identity (aka 'principal').  "
						+ "Tag body will not be evaluated.");
			}
			return SKIP_BODY;
		}
	}
}
