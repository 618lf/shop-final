package com.tmt.system.service;

import java.util.List;

import com.tmt.common.service.BaseServiceFacade;
import com.tmt.system.entity.User;
import com.tmt.system.entity.UserRunas;

/**
 * 用户切换 管理
 * @author 超级管理员
 * @date 2016-02-19
 */
public interface UserRunasServiceFacade extends BaseServiceFacade<UserRunas, Long>{
	
	/**
	 * 保存
	 */
	public void grant(UserRunas userRunas);
	
	/**
	 * 删除
	 */
	public void revoke(UserRunas userRunas);
	
	/**
	 * 删除
	 */
	public void revoke(List<UserRunas> userRunass);
	
	/**
	 * 是否存在当前权限
	 * @param userRunas
	 * @return
	 */
	public boolean exists(UserRunas userRunas);
	
	/**
	 * 查询已授权的用户
	 * @return
	 */
	public List<UserRunas> queryGrantUsers(User user);
	
	/**
	 * 查询已授权的用户
	 * @return
	 */
	public List<UserRunas> queryRunasUsers(User user);
}