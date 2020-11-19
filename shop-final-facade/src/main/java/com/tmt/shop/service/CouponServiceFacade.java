package com.tmt.shop.service;

import java.util.List;

import com.tmt.core.service.BaseServiceFacade;
import com.tmt.shop.entity.Coupon;
import com.tmt.shop.entity.CouponCode;
import com.tmt.shop.entity.CouponFission;
import com.tmt.shop.entity.CouponProduct;
import com.tmt.system.entity.User;

/**
 * 优惠券 管理
 * @author 超级管理员
 * @date 2016-11-26
 */
public interface CouponServiceFacade extends BaseServiceFacade<Coupon,Long> {
	
	/**
	 * 保存
	 */
	public void save(Coupon coupon);
	
	/**
	 * 删除
	 */
	public void delete(List<Coupon> coupons);
	
	/**
	 * 更新领取数量
	 */
	public void updateGeted(List<Coupon> coupons);
	
	
	/**
	 * 查询优惠券关联的商品(简单版)
	 * @param couponId
	 * @return
	 */
	public List<CouponProduct> queryProductByCouponId(Long couponId);
	
	/**
	 * 查询优惠券关联的商品(包含商品信息)
	 * @param couponId
	 * @return
	 */
	public List<CouponProduct> queryRichProductByCouponId(Long couponId);
	
	
	/**
	 * 查询用户可用的优惠券
	 * @param user
	 * @return
	 */
	public List<CouponCode> queryUserEnabledCoupons(User user);
	
	/**
	 * 查询一个
	 * @param codeId
	 * @return
	 */
	public CouponCode getUserEnabledCoupon(User user, Long codeId);
	
	/**
	 * 根据优惠码获取记录
	 * @param boolean
	 * @return
	 */
	public CouponCode assignOneCode(User user, String code);
	
	/**
	 * 根据优惠券获取记录并锁定记录
	 * @param boolean
	 * @return
	 */
	public CouponCode assignOneCode(Long couponId, User user);
	
	/**
	 * 分配红包 -- 裂变
	 * @param user
	 * @param orderId
	 * @return
	 */
	public CouponFission giveFissionRedenvelope(User user, Long fissionId);
}