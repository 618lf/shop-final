package com.tmt.shop.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.shop.entity.OrderAppraise;

/**
 * 订单评价 管理
 * @author 超级管理员
 * @date 2017-04-10
 */
@Repository("shopOrderAppraiseDao")
public class OrderAppraiseDao extends BaseDaoImpl<OrderAppraise,Long> {}
