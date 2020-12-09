package com.tmt.shop.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.shop.entity.CouponProduct;

/**
 * 优惠商品 管理
 * @author 超级管理员
 * @date 2016-11-26
 */
@Repository("shopCouponProductDao")
public class CouponProductDao extends BaseDaoImpl<CouponProduct,Long> {}
