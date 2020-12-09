package com.tmt.shop.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.shop.entity.RankCoupon;

/**
 * 等级赠送优惠券 管理
 * @author 超级管理员
 * @date 2017-05-11
 */
@Repository("shopRankCouponDao")
public class RankCouponDao extends BaseDaoImpl<RankCoupon,Long> {}
