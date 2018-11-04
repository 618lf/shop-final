package com.tmt.system.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tmt.common.persistence.BaseDaoImpl;
import com.tmt.system.entity.RoleMenu;

@Repository
public class RoleMenuDao extends BaseDaoImpl<RoleMenu, Long>{

	public List<RoleMenu> findByRoleId(Long roleId){
		return this.queryForList("findByRoleId", roleId);
	}
	public List<RoleMenu> findByMenuId(Long menuId){
		return this.queryForList("findByMenuId", menuId);
	}
}