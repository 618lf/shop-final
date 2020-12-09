package com.tmt.shop.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.core.persistence.BaseDao;
import com.tmt.core.persistence.Page;
import com.tmt.core.persistence.PageParameters;
import com.tmt.core.service.BaseService;
import com.tmt.core.utils.Maps;
import com.tmt.core.utils.time.DateUtils;
import com.tmt.shop.dao.CouponCodeDao;
import com.tmt.shop.entity.Coupon;
import com.tmt.shop.entity.CouponCode;
import com.tmt.system.entity.User;

/**
 * 优惠码 管理
 * @author 超级管理员
 * @date 2016-11-26
 */
@Service("shopCouponCodeService")
public class CouponCodeService extends BaseService<CouponCode,Long> implements CouponCodeServiceFacade{
	
	@Autowired
	private CouponCodeDao couponCodeDao;
	
	@Override
	protected BaseDao<CouponCode, Long> getBaseDao() {
		return couponCodeDao;
	}
	
	/**
	 * 保存
	 */
	@Transactional
	public void save(List<CouponCode> couponCodes) {
		this.batchInsert(couponCodes);
	}
	
	/**
	 * 更新领取信息
	 * @param couponCodes
	 */
	@Transactional
	public void updateUserInfo(CouponCode code){
		this.update("updateUserInfo", code);
	}
	
	/**
	 * 更新领取信息
	 * @param couponCodes
	 */
	@Transactional
	public boolean updateUsed(CouponCode code){
		int num = this.update("updateUsed", code);
		return num == 1;
	}
	
	/**
	 * 更新领取信息
	 * @param couponCodes
	 */
	@Transactional
	public boolean updateUnUsed(CouponCode code){
		int num = this.update("updateUnUsed", code);
		return num == 1;
	}
	
	/**
	 * 删除(根据优惠券来删除所有的优惠码)
	 */
	@Transactional
	public void delete(Long couponId) {
		CouponCode code = new CouponCode();
		code.setCoupon(couponId);
		super.delete(code);
	}
	
	/**
	 * 用户是否还可已分配优惠券
	 * @param id
	 * @return
	 */
	public Boolean canUserAssigned(Coupon coupon, User user){
		CouponCode code = new CouponCode();
		code.setCoupon(coupon.getId()); code.setUserId(user.getId());
		int count = this.countByCondition("countUserAssigned", code);
		// 领取张数小于设置的张数或设置的张数为0
		return count < coupon.getGetno() || coupon.getGetno() == 0;
	}
	
	/**
	 * 获取优惠券下的优惠码记录，获取时锁定记录
	 * @param id
	 * @return
	 */
	@Transactional
	public CouponCode lockOneCode(long couponId){
		return this.queryForObject("lockOneCode", couponId);
	}
	
	/**
	 * 根据优惠码获取记录并锁定记录
	 * @param id
	 * @return
	 */
	public CouponCode lockOneCodeByCode(String code){
		return this.queryForObject("lockOneCodeByCode", code);
	}
	
	/**
	 * 一个用户只能获取一张分享后的优惠券
	 * @param id
	 * @return
	 */
	public CouponCode fetchOneCodeByFission(User user, long fissionId){
		CouponCode code = new CouponCode();
		code.setFissionId(fissionId); code.setUserId(user.getId());
		return this.queryForObject("fetchOneCodeByFission", code);
	}
	
	/**
	 * 获取优惠券下的优惠码记录，获取时锁定记录
	 * @param id
	 * @return
	 */
	@Transactional
	public CouponCode lockOneCodeByFission(long fissionId){
		return this.queryForObject("lockOneCodeByFission", fissionId);
	}
	
	/**
	 * 用户可用的券码 -- 最多获取20个
	 * @param user
	 * @return
	 */
	public List<CouponCode> queryUserEnabledCoupons(User user) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("now", DateUtils.getTimeStampNow());
		params.put("userId", user.getId());
		return this.queryForLimitList("queryUserEnabledCoupons", params, 20);
	}
	
	/**
	 * 用户可用的券码 -- 最多获取20个
	 * @param user
	 * @return
	 */
	public CouponCode getUserEnabledCoupon(User user, Long id) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("now", DateUtils.getTimeStampNow());
		params.put("userId", user.getId());
		params.put("id", id);
		return this.queryForObject("getUserEnabledCoupon", params);
	}
	
	/**
	 * 用户优惠券
	 * @param user
	 * @param page
	 * @return
	 */
	public Page queryUserPage(User user, PageParameters param) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("userId", user.getId());
		return this.queryForPageList("queryUserPage", params, param);
	}
	
	/**
	 * 用户可用优惠券数量
	 * @param user	
	 * @return
	 */
	public int countUserUsableStat(User user) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("userId", user.getId());
		params.put("now", DateUtils.getTimeStampNow());
		return this.countByCondition("countUserUsableStat", params);
	}
	
	/**
	 * 设置为可用(普通)
	 * @param coupons
	 */
	@Transactional
	public void enableCoupons(List<CouponCode> coupons) {
		this.batchUpdate("updateEnabled", coupons);
	}
	
	/**
	 * 设置为可用(分裂)
	 * @param coupons
	 */
	@Transactional
	public void enableFissionCoupons(Long fissionId) {
		CouponCode code = new CouponCode();
		code.setFissionId(fissionId);
		this.update("updateFissionEnabled", code);
	}
	
	/**
	 * 所有获取的分裂红包
	 * @param fissionId
	 * @return
	 */
	public List<CouponCode> queryfissionRedenvelopes(Long fissionId) {
		return this.queryForList("queryGetedCodesByFission", fissionId);
	}
}