package com.tmt.shop.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.shop.entity.Order;

/**
 * 订单 管理
 * @author 超级管理员
 * @date 2016-01-20
 */
@Repository("shopOrderDao")
public class OrderDao extends BaseDaoImpl<Order,Long> {}