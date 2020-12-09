package com.tmt.shop.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.shop.entity.Coupon;

/**
 * 优惠券 管理
 * @author 超级管理员
 * @date 2016-11-26
 */
@Repository("shopCouponDao")
public class CouponDao extends BaseDaoImpl<Coupon,Long> {}
