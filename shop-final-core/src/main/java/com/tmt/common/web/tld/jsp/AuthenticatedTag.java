package com.tmt.common.web.tld.jsp;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.tmt.common.security.utils.SecurityUtils;

/**
 * 是否是登陆用户
 * @author lifeng
 */
public class AuthenticatedTag extends SecureTag {

	private static final long serialVersionUID = 8811885027224602644L;

	@Override
	public int onDoStartTag() throws JspException {
		if (SecurityUtils.getSubject() != null && SecurityUtils.getSubject().isAuthenticated()) {
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
