package com.tmt.core.web.tld.jsp;

/**
 * 是否具有指定的权限
 * @author lifeng
 */
public class HasRoleTag extends RoleTag{

	private static final long serialVersionUID = -2340188939236887898L;

	@Override
	protected boolean showTagBody(String p) {
		return this.hasRole(p);
	}
}
