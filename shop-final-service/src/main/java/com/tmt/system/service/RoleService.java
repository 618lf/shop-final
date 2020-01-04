package com.tmt.system.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.core.entity.TreeVO;
import com.tmt.core.persistence.BaseDao;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.service.BaseService;
import com.tmt.core.utils.Lists;
import com.tmt.core.utils.Maps;
import com.tmt.core.utils.StringUtils;
import com.tmt.system.dao.GroupRoleDao;
import com.tmt.system.dao.RoleDao;
import com.tmt.system.dao.RoleMenuDao;
import com.tmt.system.dao.RoleUserDao;
import com.tmt.system.entity.GroupRole;
import com.tmt.system.entity.Menu;
import com.tmt.system.entity.Role;
import com.tmt.system.entity.RoleMenu;
import com.tmt.system.entity.RoleUser;
import com.tmt.system.entity.User;
import com.tmt.system.service.RoleServiceFacade;

@Service
public class RoleService extends BaseService<Role, Long> implements RoleServiceFacade {

	@Autowired
	private RoleDao roleDao;
	@Autowired
	private RoleMenuDao roleMenuDao;
	@Autowired
	private RoleUserDao roleUserDao;
	@Autowired
	private GroupRoleDao groupRoleDao;
	@Autowired
	private MenuService menuService;
	@Autowired
	private GroupService groupService;
	@Autowired
	private OfficeService officeService;

	@Override
	protected BaseDao<Role, Long> getBaseDao() {
		return this.roleDao;
	}

	@Override
	public Role get(Long id) {
		Role role = super.get(id);
		List<Menu> RoleMenus = menuService.findMenusByRoleId(id);
		if (RoleMenus != null) {
			StringBuilder sbMenuIds = new StringBuilder(100);
			StringBuilder sbMenuNames = new StringBuilder(100);
			StringBuilder sbOptionIds = new StringBuilder(100);
			StringBuilder sbOptionNames = new StringBuilder(100);
			for (Menu m : RoleMenus) {
				if (m.getType() == 2) {
					sbMenuIds.append(m.getId()).append(",");
					sbMenuNames.append(m.getName()).append(",");
				} else if (m.getType() == 3) {
					sbOptionIds.append(m.getId()).append(",");
					sbOptionNames.append(m.getName()).append(",");
				}
			}
			role.setMenuIds(sbMenuIds.toString());
			role.setMenuNames(sbMenuNames.toString());
			role.setOptionIds(sbOptionIds.toString());
			role.setOptionNames(sbOptionNames.toString());
		}
		return role;
	}

	@Transactional
	public Long save(Role role) {
		if (IdGen.isInvalidId(role.getId())) {
			this.insert(role);
		} else {
			this.update(role);
		}
		Long Id = role.getId();
		List<RoleMenu> roleMenus = this.roleMenuDao.findByRoleId(role.getId());
		this.roleMenuDao.batchDelete(roleMenus);
		// 存储新权限
		roleMenus = Lists.newArrayList();
		// 菜单
		if (StringUtils.isNotEmpty(role.getMenuIds())) {
			String[] menuIds = role.getMenuIds().split(",");
			RoleMenu roleMenu = null;
			for (String menuId : menuIds) {
				if (StringUtils.isNotEmpty(menuId)) {
					roleMenu = new RoleMenu();
					roleMenu.setMenuId(Long.parseLong(menuId));
					roleMenu.setRoleId(Id);
					roleMenus.add(roleMenu);
				}
			}
		}
		// 操作
		if (StringUtils.isNotEmpty(role.getOptionIds())) {
			String[] menuIds = role.getOptionIds().split(",");
			RoleMenu roleMenu = null;
			for (String menuId : menuIds) {
				if (StringUtils.isNotEmpty(menuId)) {
					roleMenu = new RoleMenu();
					roleMenu.setMenuId(Long.parseLong(menuId));
					roleMenu.setRoleId(Id);
					roleMenus.add(roleMenu);
				}
			}
		}
		this.roleMenuDao.batchInsert(roleMenus);
		groupService.removeGroupMenus();
		return role.getId();
	}

	@Transactional
	public void delete(List<Role> roles) {
		for (Role role : roles) {
			List<RoleUser> roleUsers = this.roleUserDao.findByRoleId(role.getId());
			List<RoleMenu> roleMenus = this.roleMenuDao.findByRoleId(role.getId());
			List<GroupRole> roleGroups = this.groupRoleDao.findByRoleId(role.getId());
			this.roleUserDao.batchDelete(roleUsers);
			this.roleMenuDao.batchDelete(roleMenus);
			this.groupRoleDao.batchDelete(roleGroups);
		}
		groupService.removeGroupMenus();
		super.batchDelete(roles);
	}

	/**
	 * 查询所有的权限，包括用户单独分配的和所属用户组分配的
	 * 
	 * @param userId
	 * @return
	 */
	public List<Role> findAllByUserId(Long userId) {
		return this.queryForList("findAllByUserId", userId);
	}

	public List<Role> findByUserId(Long userId) {
		return this.queryForList("findByUserId", userId);
	}

	public List<Role> findByGroupId(Long groupId) {
		return this.queryForList("findByGroupId", groupId);
	}

	/**
	 * 根据权限获取用户的权限集合 -- 不一定要对应到菜单上（可以是权限 -- 也可以是角色）
	 * 
	 * @param permissions
	 * @return
	 */
	public List<Role> findByMenuPermission(User user, String permissions) {
		Map<String, Object> param = Maps.newHashMap();
		param.put("permissions", permissions);
		param.put("userId", user.getId());
		return this.queryForList("findByMenuPermission", param);
	}

	/**
	 * 树形结构
	 * 
	 * @return
	 */
	public List<Map<String, Object>> roleTreeSelect() {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		// 组织结构树
		List<TreeVO> orgTrees = this.officeService.findTreeList(new HashMap<String, Object>());
		// 角色树
		List<TreeVO> userTrees = this.queryForGenericsList("findTreeList", new HashMap<String, Object>());
		for (int i = 0; i < orgTrees.size(); i++) {
			TreeVO e = orgTrees.get(i);
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", "O_" + e.getId());
			map.put("pId", "O_" + e.getParent());
			map.put("name", e.getTreeName());
			map.put("chkDisabled", Boolean.TRUE);
			int iCount = this.countByCondition("officeSelectCheck", e.getId());
			if (iCount > 0) {
				map.put("chkDisabled", Boolean.FALSE);
			}
			mapList.add(map);
		}
		for (int i = 0; i < userTrees.size(); i++) {
			TreeVO e = userTrees.get(i);
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", e.getId());
			map.put("pId", "O_" + e.getParent());
			map.put("name", e.getTreeName());
			map.put("type", 1);
			mapList.add(map);
		}
		return mapList;
	}
}