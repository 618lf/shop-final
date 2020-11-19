package com.tmt.shop.service;

import java.util.List;

import com.tmt.core.service.BaseServiceFacade;
import com.tmt.shop.entity.Payment;
import com.tmt.shop.entity.Rank;
import com.tmt.shop.entity.UserRank;
import com.tmt.system.entity.User;

/**
 * 用户等级 管理
 * @author 超级管理员
 * @date 2017-02-18
 */
public interface UserRankServiceFacade extends BaseServiceFacade<UserRank,Long> {
	
	/**
	 * 会初始化用户等级
	 * @return
	 */
	public UserRank touch(Long userId);
	
	/**
	 * 简单模式
	 * @return
	 */
	public UserRank getSimple(Long userId);
	
	/**
	 * 获取用户等级且锁定
	 * @param userId
	 * @return
	 */
	public UserRank getRankandLock(Long userId);
	
	/**
	 * 保存
	 */
	public void save(UserRank userRank);
	
	/**
	 * 删除
	 */
	public void delete(List<UserRank> userRanks);
	
	/**
	 * 支付成功之后
	 */
	public boolean confirmPay(Payment payment);
	
	/**
	 * 升级到此等级
	 */
	public void upgradeRank(User user, Rank rank);
}