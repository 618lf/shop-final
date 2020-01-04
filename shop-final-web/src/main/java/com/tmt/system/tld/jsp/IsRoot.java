package com.tmt.system.tld.jsp;

import javax.servlet.jsp.JspException;

import com.tmt.core.web.tld.jsp.SecureTag;
import com.tmt.system.entity.User;
import com.tmt.system.utils.UserUtils;

/**
 * 是否是用户
 * @author lifeng
 */
public class IsRoot extends SecureTag{

	private static final long serialVersionUID = 1752570911447072813L;

	@Override
	public int onDoStartTag() throws JspException {
		User user = UserUtils.getUser();
		if (user != null && UserUtils.isAuthenticated() && user.isRoot()) {
            if (logger.isTraceEnabled()) {
            	logger.trace("Subject has known identity (aka 'principal').  " +
                        "Tag body will be evaluated.");
            }
            return EVAL_BODY_INCLUDE;
        } else {
            if (logger.isTraceEnabled()) {
            	logger.trace("Subject does not exist or have a known identity (aka 'principal').  " +
                        "Tag body will not be evaluated.");
            }
            return SKIP_BODY;
        }
	}
}
