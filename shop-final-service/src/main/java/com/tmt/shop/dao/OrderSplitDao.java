package com.tmt.shop.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.shop.entity.OrderSplit;

/**
 * 订单拆分 管理
 * @author 超级管理员
 * @date 2017-03-12
 */
@Repository("shopOrderSplitDao")
public class OrderSplitDao extends BaseDaoImpl<OrderSplit,Long> {}
