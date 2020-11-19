package com.tmt.shop.service;

import com.tmt.core.service.BaseServiceFacade;
import com.tmt.shop.entity.UserPoint;

/**
 * 积分明细 管理
 * @author 超级管理员
 * @date 2017-05-16
 */
public interface UserPointServiceFacade extends BaseServiceFacade<UserPoint,Long> {
	
	/**
	 * 添加积分
	 * @param userPoint
	 */
	public void save(UserPoint userPoint);
}