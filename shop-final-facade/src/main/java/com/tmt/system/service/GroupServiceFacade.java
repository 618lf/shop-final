package com.tmt.system.service;

import java.util.List;

import com.tmt.core.service.BaseServiceFacade;
import com.tmt.system.entity.Group;

/**
 * 用户组管理
 * @author root
 */
public interface GroupServiceFacade extends BaseServiceFacade<Group, Long>{

	
	/**
	 * 保存
	 * @param group
	 */
	public void save(Group group);
	
	/**
	 * 批量删除(删除所有关联用户和权限)
	 */
	public void delete(List<Group> groups);
	
	/**
	 * 通过用户ID 查询所属的组
	 * @param userId
	 * @return
	 */
	public List<Group> findByUserId(Long userId);
	
	/**
	 * 通过用户ID 查询所属的组
	 * @param userId
	 * @return
	 */
	public String findGroupNamesByUserId(Long userId);
	
    /**
     * 根据code获得用户组的集合
     * @param codes 用 ,分开
     * @return
     */
    public List<Group> findByCodes(String codes);
}