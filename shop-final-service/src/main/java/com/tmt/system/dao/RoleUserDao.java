package com.tmt.system.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tmt.common.persistence.BaseDaoImpl;
import com.tmt.system.entity.RoleUser;

@Repository
public class RoleUserDao extends BaseDaoImpl<RoleUser, Long> {

	public List<RoleUser> findByRoleId(Long roleId){
		return this.queryForList("findByRoleId", roleId);
	}
	public List<RoleUser> findByUserId(Long userId) {
		return this.queryForList("findByUserId", userId);
	}
}