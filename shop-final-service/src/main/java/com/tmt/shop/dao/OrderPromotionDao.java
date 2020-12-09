package com.tmt.shop.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.shop.entity.OrderPromotion;

/**
 * 订单促销 管理
 * @author 超级管理员
 * @date 2016-11-26
 */
@Repository("shopOrderPromotionDao")
public class OrderPromotionDao extends BaseDaoImpl<OrderPromotion,Long> {}