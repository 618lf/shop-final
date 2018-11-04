package com.tmt.system.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tmt.common.persistence.BaseDaoImpl;
import com.tmt.system.entity.GroupUser;

@Repository
public class GroupUserDao extends BaseDaoImpl<GroupUser, Long>{
	
	/**
	 * 查询
	 * @param groupId
	 * @return
	 */
	public List<GroupUser> findByGroupId(Long groupId){
		return this.queryForList("findByGroupId", groupId);
	}
	
	/**
	 * 查询
	 * @param groupId
	 * @return
	 */
	public List<GroupUser> findByUserId(Long userId) {
		return this.queryForList("findByUserId", userId);
	}
}