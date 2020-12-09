package com.tmt.shop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.core.persistence.BaseDao;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.service.BaseService;
import com.tmt.core.utils.Lists;
import com.tmt.shop.dao.RankCouponDao;
import com.tmt.shop.dao.RankDao;
import com.tmt.shop.entity.Rank;
import com.tmt.shop.entity.RankCoupon;
import com.tmt.shop.utils.RankUtils;

/**
 * 等级设置 管理
 * @author 超级管理员
 * @date 2017-02-18
 */
@Service("shopRankService")
public class RankService extends BaseService<Rank,Long> implements RankServiceFacade{
	
	@Autowired
	private RankDao rankDao;
	@Autowired
	private RankCouponDao couponDao;
	
	@Override
	protected BaseDao<Rank, Long> getBaseDao() {
		return rankDao;
	}
	
	/**
	 * 得到优惠券
	 * @param rankId
	 * @return
	 */
	public Rank getWithCoupons(Long id) {
		Rank rank = this.get(id);
		if (rank != null) {
			rank.setCoupons(this.queryRichCouponByRankId(id));
		}
		return rank;
	}
	
	/**
	 * 保存
	 */
	@Transactional
	public void save(Rank rank) {
		if(IdGen.isInvalidId(rank.getId())) {
			this.insert(rank);
		} else {
			this.update(rank);
		}
		this.saveCoupon(rank);
		RankUtils.clearCache();
	}
	
	// 保存赠送的优惠券
	private void saveCoupon(Rank rank) {
		List<RankCoupon> olds = this.queryCouponByRankId(rank.getId());
	    List<RankCoupon> coupons = rank.getCoupons();
	    if (coupons != null) {
	    	for(RankCoupon coupon: coupons) {
	    		coupon.setRanks(rank.getId());
	    	}
	    }
	    this.couponDao.batchDelete(olds);
	    if (coupons != null && coupons.size() != 0) {
	    	this.couponDao.batchInsert(coupons);
	    }
	}
	
	/**
	 * 删除
	 */
	@Transactional
	public void delete(List<Rank> ranks) {
		this.batchDelete(ranks);
		List<RankCoupon> coupons = Lists.newArrayList();
		for(Rank rank: ranks) {
			coupons.addAll(this.queryCouponByRankId(rank.getId()));
		}
		this.couponDao.batchDelete(coupons);
		RankUtils.clearCache();
	}
	
	/**
	 * 查询优惠券关联的商品(简单版)
	 * @param couponId
	 * @return
	 */
	public List<RankCoupon> queryCouponByRankId(Long rankId) {
		return this.couponDao.queryForList("queryCouponByRankId",rankId);
	}
	
	/**
	 * 查询优惠券关联的商品(包含商品信息)
	 * @param couponId
	 * @return
	 */
	public List<RankCoupon> queryRichCouponByRankId(Long rankId) {
		return this.couponDao.queryForList("queryRichCouponByRankId", rankId);
	}
}
