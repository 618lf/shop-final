package com.tmt.shop.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.shop.entity.OrderRank;

/**
 * 订单折扣 管理
 * @author 超级管理员
 * @date 2017-02-19
 */
@Repository("shopOrderRankDao")
public class OrderRankDao extends BaseDaoImpl<OrderRank,Long> {}
