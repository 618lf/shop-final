package com.tmt.shop.service;

import java.util.List;

import com.tmt.core.persistence.Page;
import com.tmt.core.persistence.PageParameters;
import com.tmt.core.service.BaseServiceFacade;
import com.tmt.shop.entity.CouponCode;
import com.tmt.system.entity.User;

/**
 * 优惠码 管理
 * @author 超级管理员
 * @date 2016-11-26
 */
public interface CouponCodeServiceFacade extends BaseServiceFacade<CouponCode,Long> {
	
	/**
	 * 用户优惠券列表
	 * @param user
	 * @param param
	 * @return
	 */
	public Page queryUserPage(User user, PageParameters param);
	
	
	/**
	 * 用户可用优惠券数量
	 * @param user
	 * @return
	 */
	public int countUserUsableStat(User user);
	
	/**
	 * 已获取的分裂的优惠券
	 * @param fissionId
	 * @return
	 */
	public List<CouponCode> queryfissionRedenvelopes(Long fissionId);
}