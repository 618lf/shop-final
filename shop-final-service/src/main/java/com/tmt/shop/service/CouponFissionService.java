package com.tmt.shop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.core.persistence.BaseDao;
import com.tmt.core.service.BaseService;
import com.tmt.shop.dao.CouponFissionDao;
import com.tmt.shop.entity.Coupon;
import com.tmt.shop.entity.CouponFission;
import com.tmt.system.entity.User;

/**
 * 分裂红包
 * @author root
 *
 */
@Service
public class CouponFissionService extends BaseService<CouponFission,Long>{

	@Autowired
	private CouponFissionDao fissionDao;
	
	@Override
	protected BaseDao<CouponFission, Long> getBaseDao() {
		return fissionDao;
	}
	
	/**
	 * 锁定记录防止状态变化导致数据不能更新
	 * @return
	 */
	@Transactional
	public CouponFission getSimpleAndLock(Long fissionId) {
		return this.queryForObject("getSimpleAndLock", fissionId);
	}
	
	/**
	 * 保存
	 * @param fission
	 */
	@Transactional
	public void save(List<CouponFission> fissions) {
		this.batchInsert(fissions);
	}
	
	/**
	 * 用户是否还可已分配优惠券
	 * @param id
	 * @return
	 */
	public Boolean canUserAssigned(Coupon coupon, User user){
		CouponFission fission = new CouponFission();
		fission.setCoupon(coupon.getId()); fission.setUserId(user.getId());
		int count = this.countByCondition("countUserAssigned", fission);
		// 领取张数小于设置的张数或设置的张数为0
		return count < coupon.getGetno() || coupon.getGetno() == 0;
	}
	
	/**
	 * 获取优惠券下的优惠码记录，获取时锁定记录
	 * @param id
	 * @return
	 */
	@Transactional
	public CouponFission lockOneFission(long couponId){
		return this.queryForObject("lockOneFission", couponId);
	}
	
	/**
	 * 更新领取信息
	 * @param couponCodes
	 */
	@Transactional
	public void updateUserInfo(CouponFission fission){
		this.update("updateUserInfo", fission);
	}
	
	/**
	 * 获取数量
	 * @param fission
	 */
	@Transactional
	public void updateGeted(CouponFission fission) {
		this.update("updateGeted", fission);
	}
	
	/**
	 * 获取数量
	 * @param fission
	 */
	@Transactional
	public void enableCoupons(List<CouponFission> fissions) {
		this.batchUpdate("updateEnabled", fissions);
	}
}