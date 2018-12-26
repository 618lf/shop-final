package com.tmt.system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.Constants;
import com.tmt.common.persistence.BaseDao;
import com.tmt.common.persistence.QueryCondition;
import com.tmt.common.persistence.QueryCondition.Criteria;
import com.tmt.common.persistence.incrementer.IdGen;
import com.tmt.common.service.BaseService;
import com.tmt.common.utils.CacheUtils;
import com.tmt.common.utils.Lists;
import com.tmt.common.utils.StringUtil3;
import com.tmt.system.dao.GroupDao;
import com.tmt.system.dao.GroupRoleDao;
import com.tmt.system.dao.GroupUserDao;
import com.tmt.system.entity.Group;
import com.tmt.system.entity.GroupRole;
import com.tmt.system.entity.GroupUser;

/**
 * 用户组管理
 * 
 * @author root
 */
@Service
public class GroupService extends BaseService<Group, Long> implements GroupServiceFacade {

	@Autowired
	private GroupDao groupDao;
	@Autowired
	private GroupRoleDao groupRoleDao;
	@Autowired
	private GroupUserDao groupUserDao;

	@Override
	protected BaseDao<Group, Long> getBaseDao() {
		return groupDao;
	}

	/**
	 * 保存
	 * 
	 * @param group
	 */
	@Transactional(readOnly = false)
	public void save(Group group) {
		if (IdGen.isInvalidId(group.getId())) {
			this.insert(group);
		} else {
			// 先删除,在添加
			List<GroupRole> roles = this.groupRoleDao.findByGroupId(group.getId());
			this.groupRoleDao.batchDelete(roles);
			this.update(group);
		}
		String roleIdStrs = group.getRoleIds();
		if (roleIdStrs != null) {
			List<GroupRole> groupRoles = Lists.newArrayList();
			String[] roleIds = roleIdStrs.split(",");
			GroupRole groupRole = null;
			for (String roleId : roleIds) {
				if (StringUtil3.isNotEmpty(roleId)) {
					groupRole = new GroupRole();
					groupRole.setRoleId(Long.parseLong(roleId));
					groupRole.setGroupId(group.getId());
					groupRoles.add(groupRole);
				}
			}
			this.groupRoleDao.batchInsert(groupRoles);
		}
		removeCache(group);
	}

	/**
	 * 批量删除(删除所有关联用户和权限)
	 */
	@Transactional
	public void delete(List<Group> groups) {
		for (Group group : groups) {
			List<GroupUser> users = this.groupUserDao.findByGroupId(group.getId());
			List<GroupRole> roles = this.groupRoleDao.findByGroupId(group.getId());
			this.groupUserDao.batchDelete(users);
			this.groupRoleDao.batchDelete(roles);
			removeCache(group);
		}
		super.batchDelete(groups);
	}

	/**
	 * 通过用户ID 查询所属的组
	 * 
	 * @param userId
	 * @return
	 */
	public List<Group> findByUserId(Long userId) {
		return this.queryForList("findByUserId", userId);
	}

	/**
	 * 通过用户ID 查询所属的组
	 * 
	 * @param userId
	 * @return
	 */
	public String findGroupNamesByUserId(Long userId) {
		List<Group> groups = this.findByUserId(userId);
		StringBuilder names = new StringBuilder();
		for (Group group : groups) {
			names.append(group.getName()).append(",");
		}
		if (names.length() > 0) {
			names.deleteCharAt(names.length() - 1);
		}
		return names.toString();
	}

	/**
	 * 根据code获得用户组的集合
	 * 
	 * @param codes
	 *            用 ,分开
	 * @return
	 */
	public List<Group> findByCodes(String codes) {
		String _key = new StringBuilder(Constants.CACHE_GROUP).append(codes).toString();
		List<Group> groups = CacheUtils.get(_key);
		if (groups == null) {
			String[] _codes = codes.split(",");
			List<String> lstCode = Lists.newArrayList();
			for (String code : _codes) {
				if (StringUtil3.isNotBlank(code)) {
					lstCode.add(code);
				}
			}
			QueryCondition qc = new QueryCondition();
			Criteria c = qc.getCriteria();
			c.andIn("CODE", lstCode);
			groups = this.queryByCondition(qc);
			CacheUtils.put(_key, groups);
		}
		return groups;
	}

	/**
	 * 删除指定组的缓存
	 * 
	 * @param group
	 */
	public static void removeCache(Group group) {
		if (group != null) {
			CacheUtils.evict(new StringBuilder(Constants.CACHE_GROUP_MENUS).append(group.getId()).toString());
			CacheUtils.evict(new StringBuilder(Constants.CACHE_GROUP).append("*").toString());
		}
	}

	/**
	 * 删除所有的菜单缓存
	 */
	@Transactional
	public void removeGroupMenus() {
		CacheUtils.evict(new StringBuilder(Constants.CACHE_GROUP_MENUS).append("*").toString());
	}
}