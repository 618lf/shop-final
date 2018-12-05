package com.tmt.system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmt.common.utils.Lists;
import com.tmt.system.entity.Group;
import com.tmt.system.entity.Menu;
import com.tmt.system.entity.Role;
import com.tmt.system.entity.User;

/**
 * 用户管理
 * 
 * @author root
 */
@Service
public class SystemService implements SystemServiceFacade {

	@Autowired
	private UserService userService;
	@Autowired
	private MenuService menuService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private GroupService groupService;

	@Override
	public User getUserById(Long id) {
		return userService.get(id);
	}

	@Override
	public User getUserByNo(String no) {
		return userService.findUserByNo(no);
	}

	@Override
	public User getUserByAccount(String account) {
		return userService.findUserByAccount(account);
	}

	@Override
	public String getUserHeadimgById(Long userId) {
		return userService.findUserHeadimgById(userId);
	}

	@Override
	public String getUserWechatOpenId(User user, String appId) {
		return userService.getUserWechatOpenId(user, appId);
	}

	/**
	 * 所有的菜单(单独或通过用户组分配)
	 */
	@Override
	public List<Menu> getMenus(User user) {
		List<Menu> menuList = Lists.newArrayList();
		if (user.isRoot()) {
			menuList = menuService.getAll();
		} else {
			menuList = this.findByAuthority(user.getId());
		}
		return menuList;
	}

	/**
	 * 所有的角色(单独或通过用户组分配)
	 */
	@Override
	public List<Role> getRoles(User user) {
		List<Role> roles = null;
		if (user.isRoot()) {
			roles = roleService.getAll();
		} else {
			roles = roleService.findAllByUserId(user.getId());
		}
		return roles;
	}

	private List<Menu> findByAuthority(Long userId) {
		// 用户组权限集合，并分别存于公用缓存
		List<Group> groups = this.groupService.findByUserId(userId);
		List<Menu> groupMenus = Lists.newArrayList();
		if (groups != null && groups.size() != 0) {
			for (Group group : groups) {
				// 一般会从缓存中取数据
				List<Menu> temps = this.menuService.findAuthorityByGroupId(group.getId());
				groupMenus.addAll(temps);
			}
		}
		// 个人权限集合
		List<Menu> userMenus = this.menuService.findAuthorityByUserId(userId);
		if (userMenus != null && userMenus.size() != 0) {
			groupMenus.addAll(userMenus);
		}
		// 过滤重复的
		if (groupMenus != null && groupMenus.size() != 0) {
			List<Menu> menus = Lists.newArrayList();
			for (Menu group : groupMenus) {
				Boolean bFound = Boolean.FALSE;
				for (Menu menu : menus) {
					if (group.getId().equals(menu.getId())) {
						bFound = Boolean.TRUE;
						break;
					}
				}
				if (!bFound) {
					menus.add(group);
				}
			}
			return menus;
		}
		return groupMenus;
	}

	@Override
	public List<Role> findByMenuPermission(User user, String permissions) {
		return this.roleService.findByMenuPermission(user, permissions);
	}

	@Override
	public List<User> findOfficeUsers(Long officeId) {
		return this.userService.findOfficeUsers(officeId);
	}

	@Override
	public List<User> findDepartUsers(Long officeId) {
		return this.userService.findDepartUsers(officeId);
	}

	@Override
	public String userLogin(User user, String sessionId, String loginIp) {
		return this.userService.userLogin(user, sessionId, loginIp);
	}
}