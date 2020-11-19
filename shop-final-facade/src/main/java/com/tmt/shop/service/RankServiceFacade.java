package com.tmt.shop.service;

import java.util.List;

import com.tmt.core.service.BaseServiceFacade;
import com.tmt.shop.entity.Rank;
import com.tmt.shop.entity.RankCoupon;

/**
 * 等级设置 管理
 * @author 超级管理员
 * @date 2017-02-18
 */
public interface RankServiceFacade extends BaseServiceFacade<Rank,Long> {
	
	/**
	 * 得到优惠券
	 * @param rankId
	 * @return
	 */
	public Rank getWithCoupons(Long id);
	
	/**
	 * 存储
	 * @param rank
	 */
	public void save(Rank rank);
	
	/**
	 * 删除
	 */
	public void delete(List<Rank> ranks);
	
	/**
	 * 查询优惠券关联的商品(简单版)
	 * @param couponId
	 * @return
	 */
	public List<RankCoupon> queryRichCouponByRankId(Long rankId);
}
