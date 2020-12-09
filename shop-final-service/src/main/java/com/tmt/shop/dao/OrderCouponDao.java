package com.tmt.shop.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.shop.entity.OrderCoupon;

/**
 * 订单优惠 管理
 * @author 超级管理员
 * @date 2016-07-13
 */
@Repository("shopOrderCouponDao")
public class OrderCouponDao extends BaseDaoImpl<OrderCoupon, Long> {}
