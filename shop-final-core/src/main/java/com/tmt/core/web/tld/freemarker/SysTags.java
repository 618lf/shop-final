package com.tmt.core.web.tld.freemarker;

import freemarker.template.SimpleHash;

/**
 * Shortcut for injecting the tags into Freemarker
 *
 * <p>Usage: cfg.setSharedVeriable("sys", new SecurityTags());</p>
 */
public class SysTags extends SimpleHash {
	
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("deprecation")
	public SysTags() {
        put("authenticated", new AuthenticatedTag());
        put("hasPermission", new HasPermissionTag());
        put("hasRole", new HasRoleTag());
        put("isGuest", new IsGuestTag());
        put("isRunAs", new IsRunAsTag());
        put("isUser", new IsUserTag());
        put("lacksPermission", new LacksPermissionTag());
        put("lacksRole", new LacksRoleTag());
        put("notAuthenticated", new NotAuthenticatedTag());
        put("rememberMe", new RememberMeTag());
    }
}