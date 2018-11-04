package com.tmt.system.service;

import java.util.List;
import java.util.Map;

import com.tmt.common.service.BaseServiceFacade;
import com.tmt.system.entity.Menu;

public interface MenuServiceFacade extends BaseServiceFacade<Menu, Long>{

	/**
	 * 保存
	 * @param menu
	 * @return
	 */
	public Long save(Menu menu);
	
	/**
	 * 批量删除
	 * @param menus
	 */
	public boolean delete(List<Menu> menus);
	
	/**
	 * 修改排序
	 * @param menus
	 */
	public void updateSort(List<Menu> menus);
	
	/**
	 * 得到所有的子节点
	 * @param parent
	 * @return
	 */
	public List<Menu> findByParent(Menu parent);
	
	/**
	 * 根据条件查询
	 * @param params
	 * @return
	 */
	public List<Menu> findByCondition(Map<String,Object> params);
	
	/**
	 * 根据条件查询 并显示根节点
	 * @param params
	 * @return
	 */
	public List<Menu> findAllWithRoot();
	
	/**
	 * 根据ID检查
	 * @param menuId
	 * @return
	 */
	public int treeMenuCheck(Long menuId);
	
	/**
	 * 通过角色ID查询所有的菜单
	 * @param roleId
	 * @return
	 */
	public List<Menu> findMenusByRoleId(Long roleId);
	
	/**
	 * 查询用户组的菜单权限
	 * @param userId
	 * @return
	 */
	public List<Menu> findAuthorityByGroupId(Long groupId);
	
	/**
	 * 查询用户的菜单权限 --- 单独分配给个人的
	 * @param userId
	 * @return
	 */
	public List<Menu> findAuthorityByUserId(Long userId);
}