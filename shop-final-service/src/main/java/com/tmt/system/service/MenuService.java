package com.tmt.system.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.common.persistence.BaseDao;
import com.tmt.common.persistence.incrementer.IdGen;
import com.tmt.common.service.BaseService;
import com.tmt.common.utils.CacheUtils;
import com.tmt.common.utils.Lists;
import com.tmt.common.utils.StringUtil3;
import com.tmt.system.dao.MenuDao;
import com.tmt.system.dao.RoleMenuDao;
import com.tmt.system.entity.Menu;
import com.tmt.system.entity.RoleMenu;
import com.tmt.system.entity.SystemConstant;

/**
 * 菜单管理
 * @ClassName: MenuService 
 * @author 李锋
 * @date Jun 25, 2016 2:13:28 PM 
 */
@Service
public class MenuService extends BaseService<Menu, Long> implements MenuServiceFacade{

	@Autowired
	private MenuDao menuDao;
	@Autowired
	private RoleMenuDao roleMenuDao;
	@Autowired
	private GroupService groupService;
	@Override
	protected BaseDao<Menu, Long> getBaseDao() {
		return menuDao;
	}
	
	@Transactional(readOnly = false)
	public Long save(Menu menu) {
		Menu parent = this.get(menu.getParentId());
		String oldParentIds = menu.getParentIds();
		Integer oldLevel = menu.getLevel();
		String oldPath = menu.getPath();
		menu.fillByParent(parent);
		if (!StringUtil3.isBlank(menu.getHref())) {
			menu.setType((byte)2);
		}
		if (IdGen.isInvalidId(menu.getId())) {
		    this.insert(menu);
		} else {
			this.update(menu);
			List<Menu> children = this.findByParent(menu);
			for(Menu e : children) {
				e.updateIdsByParent(menu, oldParentIds, oldPath, oldLevel);
			}
			this.batchUpdate(children);
		}
		groupService.removeGroupMenus();
		return menu.getId();
	}
	
	/**
	 * 删除
	 * @param menus
	 * @return
	 */
	@Transactional(readOnly = false)
	public boolean delete(List<Menu> menus) {
		if (menus != null && menus.size() != 0) {
			List<RoleMenu> allMenuAuths = Lists.newArrayList();
			for(Menu menu : menus ) {
				int count = this.deleteMenuCheck(menu);
				if (count > 0) {
					return Boolean.FALSE;
				}
				List<RoleMenu> menuAuths = roleMenuDao.findByMenuId(menu.getId());
				allMenuAuths.addAll(menuAuths);
			}
			if (allMenuAuths.size() != 0) {
				roleMenuDao.batchDelete(allMenuAuths);
			}
			super.batchDelete(menus);
		}
		return Boolean.TRUE;
	}
	private int deleteMenuCheck(Menu menu){
		return this.countByCondition("deleteMenuCheck", menu);
	}
	
	/**
	 * 排序
	 */
	@Transactional(readOnly = false)
	public void updateSort(List<Menu> menus ) {
		this.batchUpdate("updateSort", menus);
	}
	
	/**
	 * 得到所有的子节点
	 * @param parent
	 * @return
	 */
	public List<Menu> findByParent(Menu parent){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("PARENT_IDS",  parent.getId());
		return this.queryForList("findByCondition", params);
	}
	public List<Menu> findByCondition(Map<String,Object> params) {
		return this.queryForList("findByCondition", params);
	}
	public List<Menu> findAllWithRoot(){
		return this.queryForList("findAllWithRoot", new Object());
	}
	public int treeMenuCheck(Long menuId){
		return this.countByCondition("treeMenuCheck", menuId);
	}
	/**
	 * 通过角色ID查询所有的菜单
	 * @param roleId
	 * @return
	 */
	public List<Menu> findMenusByRoleId(Long roleId){
		return this.queryForList("findMenusByRoleId", roleId);
	}
	
	/**
	 * 查询用户组的菜单权限
	 * @param userId
	 * @return
	 */
	public List<Menu> findAuthorityByGroupId(Long groupId) {
		List<Menu> menus = CacheUtils.get(new StringBuilder(SystemConstant.CACHE_GROUP_MENUS).append(groupId).toString());
		if(menus == null) {
			menus = this.queryForList("findAuthorityByGroupId", groupId);
			if(menus == null) {menus = Lists.newArrayList();}
			CacheUtils.put(new StringBuilder(SystemConstant.CACHE_GROUP_MENUS).append(groupId).toString(), menus);
		}
		return menus;
	}
	
	/**
	 * 查询用户的菜单权限 --- 单独分配给个人的
	 * @param userId
	 * @return
	 */
	public List<Menu> findAuthorityByUserId(Long userId) {
		return this.queryForList("findAuthorityByUserId", userId);
	}
}