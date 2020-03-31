package com.tmt.core.security.permission.impl;

import java.util.List;
import java.util.Set;

import com.tmt.core.security.permission.Permission;
import com.tmt.core.utils.Lists;

/**
 * 多级别的验证
 * 
 * @author lifeng
 */
public class Permissions implements Permission{

	private List<Permission>  permissions = Lists.newArrayList();
	
	public Permissions addPermission(Permission permission) {
		permissions.add(permission);
		return this;
	}
	
	@Override
	public boolean implies(Set<String> permissions) {
		for (Permission permission: this.permissions) {
			if(!permission.implies(permissions)) {
				return false;
			}
		}
		return true;
	}
}
