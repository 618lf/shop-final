package com.tmt.system.entity;

import java.io.Serializable;

import com.tmt.common.entity.BaseEntity;

/**
 * 用户组
 * @author lifeng
 */
public class Group extends BaseEntity<Long> implements Serializable{
   
	private static final long serialVersionUID = 1L;
	private String code;
	private String roleIds;
    private String roleNames;
	public String getRoleIds() {
		return roleIds;
	}
	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
	}
	public String getRoleNames() {
		return roleNames;
	}
	public void setRoleNames(String roleNames) {
		this.roleNames = roleNames;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
}