package com.tmt.core.security.permission.impl;

import java.util.Set;

import org.springframework.util.CollectionUtils;

import com.tmt.core.security.permission.Permission;

/**
 * or 权限
 * 
 * @author lifeng
 */
public class OrPermission implements Permission {

	private String[] permissions;

	public OrPermission(String[] permissions) {
		this.permissions = permissions;
	}

	@Override
	public boolean implies(Set<String> permissions) {
		if (CollectionUtils.isEmpty(permissions)) {
			return false;
		}
		if (this.permissions != null && this.permissions.length != 0) {
			for (String p : this.permissions) {
				if (permissions.contains(p)) {
					return true;
				}
			}
		}
		return false;
	}
}
