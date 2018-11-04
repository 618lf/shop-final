package com.tmt.common.web.tld.jsp;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.tmt.common.security.utils.SecurityUtils;

/**
 * 记住我身份
 * 
 * @author lifeng
 */
public class RememberMeTag extends SecureTag {

	private static final long serialVersionUID = -5381435807322088442L;

	@Override
	public int onDoStartTag() throws JspException {
		if (SecurityUtils.getSubject().getPrincipal() != null && SecurityUtils.getSubject().isRemembered()) {
			if (logger.isTraceEnabled()) {
				logger.trace("Subject exists and is authenticated.  Tag body will be evaluated.");
			}
			return TagSupport.EVAL_BODY_INCLUDE;
		} else {
			if (logger.isTraceEnabled()) {
				logger.trace("Subject does not exist or is not authenticated.  Tag body will not be evaluated.");
			}
			return TagSupport.SKIP_BODY;
		}
	}
}
