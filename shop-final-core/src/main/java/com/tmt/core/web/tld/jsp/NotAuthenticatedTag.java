package com.tmt.core.web.tld.jsp;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.tmt.core.security.utils.SecurityUtils;

/**
 * 不是登录用户 -- 未登录 或记住我
 * @author lifeng
 *
 */
public class NotAuthenticatedTag extends SecureTag {

	private static final long serialVersionUID = 6018211357392400182L;

	@Override
	public int onDoStartTag() throws JspException {
		if (SecurityUtils.getSubject() == null || SecurityUtils.getSubject().getPrincipal() == null || SecurityUtils.getSubject().isRemembered()) {
            if (logger.isTraceEnabled()) {
            	logger.trace("Subject does not exist or is not authenticated.  Tag body will be evaluated.");
            }
            return TagSupport.EVAL_BODY_INCLUDE;
        } else {
            if (logger.isTraceEnabled()) {
            	logger.trace("Subject exists and is authenticated.  Tag body will not be evaluated.");
            }
            return TagSupport.SKIP_BODY;
        }
	}

}
