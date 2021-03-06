package com.tmt.shop.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.shop.entity.CouponCode;

/**
 * 优惠码 管理
 * @author 超级管理员
 * @date 2016-11-26
 */
@Repository("shopCouponCodeDao")
public class CouponCodeDao extends BaseDaoImpl<CouponCode,Long> {}
