package com.tmt.core.web.tld.jsp;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.tmt.core.security.utils.SecurityUtils;

/**
 * 用户权限
 * 
 * @author lifeng
 */
public abstract class PermissionTag extends SecureTag {

	private static final long serialVersionUID = 5319391417710697346L;

	private String name = null;

	protected void verifyAttributes() throws JspException {
		String permission = getName();

		if (permission == null || permission.length() == 0) {
			String msg = "The 'name' tag attribute must be set.";
			throw new JspException(msg);
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int onDoStartTag() throws JspException {
		String p = getName();
		boolean show = showTagBody(p);
		if (show) {
			return TagSupport.EVAL_BODY_INCLUDE;
		} else {
			return TagSupport.SKIP_BODY;
		}
	}
	
	protected boolean isPermitted(String permission) {
        return SecurityUtils.getSubject().isPermitted(permission);
    }

    protected abstract boolean showTagBody(String p);
}
