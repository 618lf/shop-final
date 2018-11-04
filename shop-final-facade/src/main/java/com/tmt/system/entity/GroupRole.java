package com.tmt.system.entity;

import java.io.Serializable;

/**
 * 用户组角色
 * @author lifeng
 */
public class GroupRole implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private Long groupId;
    private Long roleId;
	public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
}