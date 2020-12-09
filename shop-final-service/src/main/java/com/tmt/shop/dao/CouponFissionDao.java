package com.tmt.shop.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.shop.entity.CouponFission;

/**
 * 优惠券裂变 管理
 * @author 超级管理员
 * @date 2016-11-26
 */
@Repository("shopCouponFissionDao")
public class CouponFissionDao extends BaseDaoImpl<CouponFission,Long> {}
