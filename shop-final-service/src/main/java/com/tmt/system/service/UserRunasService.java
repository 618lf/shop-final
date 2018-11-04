package com.tmt.system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.common.persistence.BaseDao;
import com.tmt.common.service.BaseService;
import com.tmt.system.dao.UserRunasDao;
import com.tmt.system.entity.User;
import com.tmt.system.entity.UserRunas;
import com.tmt.system.service.UserRunasServiceFacade;

/**
 * 用户切换 管理
 * @author 超级管理员
 * @date 2016-02-19
 */
@Service
public class UserRunasService extends BaseService<UserRunas,Long> implements UserRunasServiceFacade{
	
	@Autowired
	private UserRunasDao userRunasDao;
	
	@Override
	protected BaseDao<UserRunas, Long> getBaseDao() {
		return userRunasDao;
	}
	
	/**
	 * 保存
	 */
	@Transactional
	public void grant(UserRunas userRunas) {
		this.insert(userRunas);
	}
	
	/**
	 * 删除
	 */
	@Transactional
	public void revoke(UserRunas userRunas) {
		this.delete(userRunas);
	}
	
	/**
	 * 删除
	 */
	@Transactional
	public void revoke(List<UserRunas> userRunass) {
		this.batchDelete(userRunass);
	}
	
	/**
	 * 是否存在当前权限
	 * @param userRunas
	 * @return
	 */
	public boolean exists(UserRunas userRunas) {
		int count = this.countByCondition("exists", userRunas);
		return count == 1;
	}
	@Override
	public List<UserRunas> queryGrantUsers(User user) {
		return this.queryForList("findGrantUsers", user.getId());
	}
	@Override
	public List<UserRunas> queryRunasUsers(User user) {
		return this.queryForList("findCanRunasUsers", user.getId());
	}
}