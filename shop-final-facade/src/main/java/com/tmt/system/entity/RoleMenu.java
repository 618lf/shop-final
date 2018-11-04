package com.tmt.system.entity;

import java.io.Serializable;

/**
 * 菜单权限
 * @author lifeng
 */
public class RoleMenu implements Serializable{
  
	private static final long serialVersionUID = 1L;
	private Long roleId;
    private Long menuId;
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	public Long getMenuId() {
		return menuId;
	}
	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}
}