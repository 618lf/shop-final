package com.tmt.system.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.system.entity.GroupRole;

@Repository
public class GroupRoleDao extends BaseDaoImpl<GroupRole, Long>{

	/**
	 * 根据用户组查询用户组所拥有的权限
	 * @param groupId
	 * @return
	 */
	public List<GroupRole> findByGroupId(Long groupId){
		return this.queryForList("findByGroupId", groupId);
	}
	
	/**
	 * 通过角色ID用户组权限集合
	 * @param groupId
	 * @return
	 */
	public List<GroupRole> findByRoleId(Long groupId){
		return this.queryForList("findByRoleId", groupId);
	}
}
