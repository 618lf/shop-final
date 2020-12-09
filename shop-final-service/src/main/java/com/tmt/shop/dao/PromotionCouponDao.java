package com.tmt.shop.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.shop.entity.PromotionCoupon;

/**
 * 促销优惠券 管理
 * @author 超级管理员
 * @date 2016-11-26
 */
@Repository("shopPromotionCouponDao")
public class PromotionCouponDao extends BaseDaoImpl<PromotionCoupon,Long> {}
