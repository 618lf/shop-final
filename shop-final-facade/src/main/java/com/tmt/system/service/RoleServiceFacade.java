package com.tmt.system.service;

import java.util.List;
import java.util.Map;

import com.tmt.core.service.BaseServiceFacade;
import com.tmt.system.entity.Role;

public interface RoleServiceFacade extends BaseServiceFacade<Role, Long>{
	
	/**
	 * 保存
	 * @param role
	 * @return
	 */
	public Long save(Role role);
	
	/**
	 * 批量删除
	 * @param roles
	 */
	public void delete(List<Role> roles );
	
	
	/**
	 * 查询所有的权限，包括用户单独分配的和所属用户组分配的
	 * @param userId
	 * @return
	 */
	public List<Role> findAllByUserId(Long userId);
	
	/**
	 * 根据用户ID查询
	 * @param userId
	 * @return
	 */
	public List<Role> findByUserId(Long userId);
	
	/**
	 * 根据组查询
	 * @param groupId
	 * @return
	 */
	public List<Role> findByGroupId(Long groupId);
	
	/**
	 * 树形结构
	 * @return
	 */
	public List<Map<String, Object>> roleTreeSelect();
}