package com.tmt.system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.tmt.common.persistence.Page;
import com.tmt.common.persistence.PageParameters;
import com.tmt.common.persistence.QueryCondition;
import com.tmt.common.persistence.QueryCondition.Criteria;
import com.tmt.common.service.BaseService;
import com.tmt.common.utils.Lists;
import com.tmt.common.utils.StringUtils;
import com.tmt.system.entity.Role;
import com.tmt.system.entity.User;
import com.tmt.system.entity.Role.DataScope;

/**
 * 数据权限支持
 * @author lifeng
 */
public abstract class DataScopeService<T, PK> extends BaseService<T, PK>{

	@Autowired
	private SystemServiceFacade systemService;
	
	/**
	 * 添加数据权限过滤的条件
	 * resources --- 资源名称,对应到menu中的 permission
	 * tablePre -- 表前缀不带.
	 */
	public Page queryForDataScopePage(User user, String permissions, String tablePre, QueryCondition qc, PageParameters param) {
		//需要考虑缓存的支持  -- 需要缓存支持
		qc = this.getDataScopeCondition(user, permissions, tablePre, qc);
    	return this.getBaseDao().queryForPage(qc, param);
    }
	
	/**
	 * 添加数据权限过滤的条件
	 * resources --- 资源名称,对应到menu中的 permission
	 * tablePre -- 表前缀不带.
	 */
	public List<T> queryForDataScopeList(User user, String permissions, String tablePre, QueryCondition qc) {
		return this.queryForDataScopeList(user, "findByCondition", permissions, tablePre, qc);
	}
	
	/**
	 * 指定sql
	 * @param sql
	 * @param permissions
	 * @param tablePre
	 * @param qc
	 * @return
	 */
	public List<T> queryForDataScopeList(User user, String sql, String permissions, String tablePre, QueryCondition qc) {
		//需要考虑缓存的支持  -- 需要缓存支持
		qc = this.getDataScopeCondition(user, permissions, tablePre, qc);
		return this.getBaseDao().queryForList(sql, qc);
	}
	
	/**
	 * 查询其他对象
	 * @param sql
	 * @param permissions
	 * @param tablePre
	 * @param qc
	 * @return
	 */
	public <E> List<E> queryForDataScopeGenericsList(User user, String sql, String permissions, String tablePre, QueryCondition qc) {
		//需要考虑缓存的支持  -- 需要缓存支持
		qc = this.getDataScopeCondition(user, permissions, tablePre, qc);
		return this.getBaseDao().queryForGenericsList(sql, qc);
	}
	
	/**
	 * 获取数据库范围的查询条件
	 * @param permissions
	 * @param tablePre
	 * @param qc
	 * @return
	 */
	protected QueryCondition getDataScopeCondition(User user, String permissions, String tablePre, QueryCondition qc) {
		String _tablePre = StringUtils.isNotBlank(tablePre)?(tablePre + "."):"";
		//需要考虑缓存的支持  -- 需要缓存支持
		List<Role> roles = systemService.findByMenuPermission(user, permissions);
		Role one = (roles != null && !roles.isEmpty())?roles.get(0):null;
		DataScope dataScope = one!=null?one.getDataScope():null;
		Criteria c = qc.getCriteria();
		if( one == null || DataScope.USER == dataScope ) {//自己创建
			c.andEqualTo(_tablePre + "CREATE_ID", user.getId());
		} else if(DataScope.OFFICE == dataScope) {//本小组
			List<User> users = this.systemService.findOfficeUsers(user.getOfficeId());
			createIdIn(_tablePre, c, users);
		} else if(DataScope.DEPART == dataScope) {//所在的部门
			List<User> users = this.systemService.findDepartUsers(user.getOfficeId());
			createIdIn(_tablePre, c, users);
		} else if(DataScope.ALL == dataScope) { }//所有数据不需要添加条件
		return qc;
	}
	
	//用户in提高性能
	private void createIdIn(String tablePre, Criteria c, List<User> users) {
		List<Long> ids = Lists.newArrayList();
		if(users != null && !users.isEmpty()) {
			for(User user: users) {
				ids.add(user.getId());
			}
		}
		c.andIn(tablePre+"CREATE_ID", ids);
	}
}