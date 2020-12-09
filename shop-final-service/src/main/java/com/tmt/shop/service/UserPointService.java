package com.tmt.shop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.core.persistence.BaseDao;
import com.tmt.core.service.BaseService;
import com.tmt.shop.dao.UserPointDao;
import com.tmt.shop.entity.UserPoint;

/**
 * 积分明细 管理
 * @author 超级管理员
 * @date 2017-05-16
 */
@Service("shopUserPointService")
public class UserPointService extends BaseService<UserPoint,Long> implements UserPointServiceFacade{
	
	@Autowired
	private UserPointDao userPointDao;
	
	@Override
	protected BaseDao<UserPoint, Long> getBaseDao() {
		return userPointDao;
	}
	
	/**
	 * 保存
	 */
	@Transactional
	public void save(UserPoint userPoint) {
		this.insert(userPoint);
	}
	
	/**
	 * 删除
	 */
	@Transactional
	public void delete(List<UserPoint> userPoints) {
		this.batchDelete(userPoints);
	}
}