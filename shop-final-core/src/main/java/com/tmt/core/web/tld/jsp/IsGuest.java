package com.tmt.core.web.tld.jsp;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.tmt.core.security.utils.SecurityUtils;

/**
 * 是否是游客
 * 
 * @author lifeng
 */
public class IsGuest extends SecureTag {

	private static final long serialVersionUID = -4706767553191621523L;

	@Override
	public int onDoStartTag() throws JspException {
		if (SecurityUtils.getSubject() == null || SecurityUtils.getSubject().getPrincipal() == null) {
			if (logger.isTraceEnabled()) {
				logger.trace("Subject does not exist or does not have a known identity (aka 'principal').  "
						+ "Tag body will be evaluated.");
			}
			return TagSupport.EVAL_BODY_INCLUDE;
		} else {
			if (logger.isTraceEnabled()) {
				logger.trace("Subject exists or has a known identity (aka 'principal').  "
						+ "Tag body will not be evaluated.");
			}
			return TagSupport.SKIP_BODY;
		}
	}
}
