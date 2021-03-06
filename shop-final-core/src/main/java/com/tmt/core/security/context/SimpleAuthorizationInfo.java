package com.tmt.core.security.context;

import java.util.Set;

import com.tmt.core.utils.Sets;

/**
 * 简单的权限信息
 * 
 * @author lifeng
 */
public class SimpleAuthorizationInfo implements AuthorizationInfo {

	private static final long serialVersionUID = 1L;

	private Set<String> permissions = Sets.newHashSet(2);
	private Set<String> roles = Sets.newHashSet(2);

	@Override
	public Set<String> getRoles() {
		return roles;
	}

	@Override
	public Set<String> getPermissions() {
		return permissions;
	}

	public void addPermission(String permission) {
		if (this.permissions == null) {
			this.permissions = Sets.newHashSet();
		}
		this.permissions.add(permission);
	}

	public void addRole(String role) {
		if (this.roles == null) {
			this.roles = Sets.newHashSet();
		}
		this.roles.add(role);
	}
}