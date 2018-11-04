package com.tmt.common.web.tld.jsp;


/**
 * 不具有XX权限
 * @author lifeng
 *
 */
public class LacksPermissionTag extends PermissionTag {

	private static final long serialVersionUID = -4785810560001404794L;

	@Override
	protected boolean showTagBody(String p) {
		return !isPermitted(p);
	}

}
