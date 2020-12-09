package com.tmt.shop.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.shop.entity.OrderItem;

/**
 * 订单明细 管理
 * @author 超级管理员
 * @date 2016-01-20
 */
@Repository("shopOrderItemDao")
public class OrderItemDao extends BaseDaoImpl<OrderItem,Long> {}