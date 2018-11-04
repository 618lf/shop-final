package com.tmt.system.entity;

import java.io.Serializable;

/**
 * 用户权限
 * @author lifeng
 */
public class RoleUser implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private Long userId;
    private Long roleId;
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
}